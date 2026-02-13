package com.mashiverse.mashit.utils.helpers

import com.coinbase.android.nativesdk.CoinbaseWalletSDK
import com.coinbase.android.nativesdk.message.request.RequestContent
import com.coinbase.android.nativesdk.message.request.Web3JsonRPC
import com.coinbase.android.nativesdk.message.response.ActionResult
import com.mashiverse.mashit.data.models.mashi.GasEstimate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.Type
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.protocol.core.methods.response.EthCall
import org.web3j.protocol.http.HttpService
import timber.log.Timber
import java.math.BigInteger
import kotlin.coroutines.resume

object Web3Helper {
    private const val MARKETPLACE_ADDRESS: String = "0x69945bc1F0fa219d3b9063B62EB2ED6f99e3EF09"
    private const val USDC_ADDRESS: String = "0x3c499c542cEF5E3811e1192ce70d8cC03d5c3359"
    private const val CHAIN_ID = "137"

    private val web3j = Web3j.build(HttpService("https://polygon-rpc.com"))

    // --- Encoding Helpers ---

    fun encodeBuyAutoURIData(listingId: BigInteger, recipient: String): String {
        val fn = Function(
            "buyAutoURI",
            listOf(Uint256(listingId), Uint256(BigInteger.ONE), Address(recipient)),
            emptyList()
        )
        return FunctionEncoder.encode(fn)
    }

    fun encodeERC20Approve(spender: String, amount: BigInteger): String {
        val fn = Function("approve", listOf(Address(spender), Uint256(amount)), emptyList())
        return FunctionEncoder.encode(fn)
    }

    // --- Gas & Nonce Helpers ---

    suspend fun getUsdcBalance(address: String): BigInteger = withContext(Dispatchers.IO) {
        try {
            val function = Function(
                "balanceOf",
                listOf(Address(address)),
                listOf(object : TypeReference<Uint256>() {})
            )
            val encodedFunction = FunctionEncoder.encode(function)

            val response: EthCall = web3j.ethCall(
                Transaction.createEthCallTransaction(address, USDC_ADDRESS, encodedFunction),
                DefaultBlockParameterName.LATEST
            ).send()

            val results: List<Type<*>> = FunctionReturnDecoder.decode(
                response.value,
                function.outputParameters
            )

            if (results.isNotEmpty()) {
                results[0].value as BigInteger
            } else {
                BigInteger.ZERO
            }
        } catch (e: Exception) {
            Timber.e("Failed to fetch USDC balance: ${e.message}")
            BigInteger.ZERO
        }
    }

    /**
     * Helper to check if user has enough USDC for the price.
     * @param price The price in USDC (human readable, e.g., 10.5)
     */
    suspend fun hasEnoughUsdc(address: String, price: Double): Boolean {
        val balance = getUsdcBalance(address)
        val required = (price * 1_000_000).toLong().toBigInteger()
        return balance >= required
    }

    private suspend fun getNonce(fromAddress: String): Int = withContext(Dispatchers.IO) {
        val txCount =
            web3j.ethGetTransactionCount(fromAddress, DefaultBlockParameterName.LATEST).send()
        txCount.transactionCount.toInt()
    }

    suspend fun calculateGasEstimate(
        fromAddress: String,
        toAddress: String,
        data: String
    ): GasEstimate = withContext(Dispatchers.IO) {
        try {
            val feeHistory = web3j.ethFeeHistory(1, DefaultBlockParameterName.LATEST, listOf(20.0))
                .send().feeHistory
            val baseFee = feeHistory.baseFeePerGas.last()
            val priorityFee = BigInteger("30000000000")
            val maxFee = baseFee.add(priorityFee)

            val transaction = Transaction.createEthCallTransaction(fromAddress, toAddress, data)
            val estimateGas = web3j.ethEstimateGas(transaction).send().amountUsed

            val bufferedLimit =
                estimateGas.multiply(BigInteger.valueOf(120)).divide(BigInteger.valueOf(100))

            GasEstimate(
                gasLimit = bufferedLimit.toString(),
                maxFeePerGas = maxFee.toString(),
                maxPriorityFeePerGas = priorityFee.toString()
            )
        } catch (e: Exception) {
            Timber.e("Gas estimation failed: ${e.message}")
            GasEstimate("300000", "250000000000", "40000000000")
        }
    }

    // --- Main Suspend Actions ---

    suspend fun mint(
        client: CoinbaseWalletSDK,
        fromAddress: String,
        listingId: String,
        price: Double,
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val usdcPrice = (price * 1_000_000).toLong().toBigInteger()
            val approveData = encodeERC20Approve(MARKETPLACE_ADDRESS, usdcPrice)

            val gas = calculateGasEstimate(fromAddress, USDC_ADDRESS, approveData)
            val nonce = getNonce(fromAddress)

            val action = Web3JsonRPC.SendTransaction(
                fromAddress = fromAddress,
                toAddress = USDC_ADDRESS,
                weiValue = "0",
                data = approveData,
                chainId = CHAIN_ID,
                nonce = nonce,
                gasLimit = gas.gasLimit,
                maxFeePerGas = gas.maxFeePerGas,
                maxPriorityFeePerGas = gas.maxPriorityFeePerGas,
                gasPriceInWei = null,
                actionSource = null
            ).action()

            // Convert Coinbase SDK callback to suspend flow
            val success = suspendCancellableCoroutine<Boolean> { continuation ->
                client.makeRequest(RequestContent.Request(listOf(action))) { result ->
                    result.onSuccess {
                        Timber.d("✅ Approval Submitted.")
                        continuation.resume(true)
                    }
                    result.onFailure {
                        Timber.e("❌ Approval Failed: ${it.message}")
                        continuation.resume(false)
                    }
                }
            }

            if (success) {
                delay(4000) // Wait for state propagation
                return@withContext executeMint(client, listingId, fromAddress)
            }
            false
        } catch (e: Exception) {
            Timber.e("Mint process error: ${e.message}")
            false
        }
    }

    suspend fun executeMint(
        client: CoinbaseWalletSDK,
        listingId: String,
        fromAddress: String,
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val mintData = encodeBuyAutoURIData(BigInteger(listingId), fromAddress)
            val gas = calculateGasEstimate(fromAddress, MARKETPLACE_ADDRESS, mintData)
            val nonce = getNonce(fromAddress)

            val action = Web3JsonRPC.SendTransaction(
                fromAddress = fromAddress,
                toAddress = MARKETPLACE_ADDRESS,
                weiValue = "0",
                data = mintData,
                chainId = CHAIN_ID,
                nonce = nonce,
                gasLimit = gas.gasLimit,
                maxFeePerGas = gas.maxFeePerGas,
                maxPriorityFeePerGas = gas.maxPriorityFeePerGas,
                gasPriceInWei = null,
                actionSource = null
            ).action()

            suspendCancellableCoroutine { continuation ->
                client.makeRequest(RequestContent.Request(listOf(action))) { result ->
                    result.onSuccess { actions ->
                        val res = actions.firstOrNull() as? ActionResult.Result
                        Timber.d("✅ buyAutoURI Success! Hash: ${res?.value}")
                        continuation.resume(true)
                    }
                    result.onFailure {
                        Timber.e("❌ buyAutoURI Failed: ${it.message}")
                        continuation.resume(false)
                    }
                }
            }
        } catch (e: Exception) {
            Timber.e("ExecuteMint Error: ${e.message}")
            false
        }
    }
}