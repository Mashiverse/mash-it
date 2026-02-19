package com.mashiverse.mashit.utils.helpers

import com.coinbase.android.nativesdk.CoinbaseWalletSDK
import com.coinbase.android.nativesdk.message.request.RequestContent
import com.coinbase.android.nativesdk.message.request.Web3JsonRPC
import com.coinbase.android.nativesdk.message.response.ActionResult
import com.mashiverse.mashit.BuildConfig
import com.mashiverse.mashit.data.models.dialog.DialogContent
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
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.protocol.http.HttpService
import timber.log.Timber
import java.math.BigInteger
import kotlin.coroutines.resume

object Web3Helper {
    private const val MARKETPLACE_ADDRESS: String = "0x69945bc1F0fa219d3b9063B62EB2ED6f99e3EF09"
    private const val USDC_ADDRESS: String = "0x3c499c542cEF5E3811e1192ce70d8cC03d5c3359"
    private const val CHAIN_ID = "137"

    private val MAX_UINT256 = BigInteger("f".repeat(64), 16)
    private val web3j = Web3j.build(HttpService("https://polygon-mainnet.g.alchemy.com/v2/${BuildConfig.ALCHEMY_API_KEY}"))

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

    // --- Contract State Helpers ---

    private suspend fun canUserMint(listingId: BigInteger, userAddress: String): Boolean = withContext(Dispatchers.IO) {
        try {
            Timber.tag("GG").d("Simulating mint for Listing: $listingId, User: $userAddress")

            // 1. Prepare the exact data payload for buyAutoURI(listingId, 1, recipient)
            val mintData = encodeBuyAutoURIData(listingId, userAddress)

            // 2. Simulate the transaction using ethEstimateGas
            // This doesn't cost any gas and doesn't open the wallet
            val transaction = Transaction.createEthCallTransaction(
                userAddress,           // from
                MARKETPLACE_ADDRESS,   // to
                mintData               // encoded function call
            )

            val estimateResponse = web3j.ethEstimateGas(transaction).send()

            if (estimateResponse.hasError()) {
                val errorMessage = estimateResponse.error.message.lowercase()
                Timber.tag("GG").w("Simulation failed: $errorMessage")

                // Most marketplace contracts revert with a specific message like "max buy reach" or "already minted"
                // You can check for specific strings if you want, or just assume failure means limit/sold out
                if (errorMessage.contains("revert") || errorMessage.contains("limit") || errorMessage.contains("max")) {
                    Timber.tag("GG").w("BLOCKING: Contract execution would revert (likely limit reached)")
                    return@withContext false
                }
            } else {
                Timber.tag("GG").d("Simulation success. Gas needed: ${estimateResponse.amountUsed}")
                return@withContext true
            }

            true // Default to true if the error isn't a clear revert
        } catch (e: Exception) {
            Timber.tag("GG").e(e, "Simulation crashed")
            true
        }
    }

    // --- Balance & Allowance Helpers ---

    suspend fun getUsdcBalance(address: String): BigInteger = withContext(Dispatchers.IO) {
        try {
            val function = Function("balanceOf", listOf(Address(address)), listOf(object : TypeReference<Uint256>() {}))
            val response = web3j.ethCall(Transaction.createEthCallTransaction(address, USDC_ADDRESS, FunctionEncoder.encode(function)), DefaultBlockParameterName.LATEST).send()
            val results = FunctionReturnDecoder.decode(response.value, function.outputParameters)
            if (results.isNotEmpty()) results[0].value as BigInteger else BigInteger.ZERO
        } catch (e: Exception) { BigInteger.ZERO }
    }

    suspend fun getUsdcAllowance(owner: String): BigInteger = withContext(Dispatchers.IO) {
        try {
            val function = Function("allowance", listOf(Address(owner), Address(MARKETPLACE_ADDRESS)), listOf(object : TypeReference<Uint256>() {}))
            val response = web3j.ethCall(Transaction.createEthCallTransaction(owner, USDC_ADDRESS, FunctionEncoder.encode(function)), DefaultBlockParameterName.LATEST).send()
            val results = FunctionReturnDecoder.decode(response.value, function.outputParameters)
            if (results.isNotEmpty()) results[0].value as BigInteger else BigInteger.ZERO
        } catch (e: Exception) { BigInteger.ZERO }
    }

    // --- Gas & Nonce Helpers ---

    private suspend fun getNonce(fromAddress: String): BigInteger = withContext(Dispatchers.IO) {
        web3j.ethGetTransactionCount(fromAddress, DefaultBlockParameterName.PENDING).send().transactionCount
    }

    suspend fun calculateGasEstimate(fromAddress: String, toAddress: String, data: String): GasEstimate = withContext(Dispatchers.IO) {
        try {
            val feeHistory = web3j.ethFeeHistory(1, DefaultBlockParameterName.LATEST, listOf(20.0)).send().feeHistory
            val baseFee = feeHistory.baseFeePerGas.last()
            val priorityFee = web3j.ethMaxPriorityFeePerGas().send().maxPriorityFeePerGas.multiply(BigInteger.valueOf(120)).divide(BigInteger.valueOf(100))
            val maxFee = baseFee.multiply(BigInteger.valueOf(2)).add(priorityFee)
            val estimateGas = web3j.ethEstimateGas(Transaction.createEthCallTransaction(fromAddress, toAddress, data)).send().amountUsed
            GasEstimate(estimateGas.multiply(BigInteger.valueOf(120)).divide(BigInteger.valueOf(100)).toString(), maxFee.toString(), priorityFee.toString())
        } catch (e: Exception) { GasEstimate("350000", "500000000000", "50000000000") }
    }

    private suspend fun waitForReceipt(txHash: String): Boolean = withContext(Dispatchers.IO) {
        repeat(30) {
            val receipt = web3j.ethGetTransactionReceipt(txHash).send().transactionReceipt
            if (receipt.isPresent) return@withContext receipt.get().isStatusOK
            delay(4000)
        }
        false
    }

    // --- Main Optimized Action ---

    suspend fun mint(
        client: CoinbaseWalletSDK,
        fromAddress: String,
        listingId: String,
        price: Double,
        onMintFailure: (DialogContent) -> Unit
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            // 1. STRICT CHECK: Max count per user (Check this BEFORE anything else)
            val lid = BigInteger(listingId)
            if (!canUserMint(lid, fromAddress)) {
                onMintFailure.invoke(DialogContent(
                    title = "Minted max count",
                    text = "You have already reached the maximum allowed mints for this listing."
                ))
                return@withContext false
            }

            val usdcPrice = (price * 1_000_000).toLong().toBigInteger()

            // 2. Check Balance
            if (getUsdcBalance(fromAddress) < usdcPrice) {
                onMintFailure.invoke(DialogContent(title = "Mint process error", text = "Insufficient USDC balance."))
                return@withContext false
            }

            // 3. Check Allowance
            if (getUsdcAllowance(fromAddress) < usdcPrice) {
                val approveData = encodeERC20Approve(MARKETPLACE_ADDRESS, MAX_UINT256)
                val gas = calculateGasEstimate(fromAddress, USDC_ADDRESS, approveData)

                val action = Web3JsonRPC.SendTransaction(
                    fromAddress = fromAddress,
                    toAddress = USDC_ADDRESS,
                    weiValue = "0",
                    data = approveData,
                    chainId = CHAIN_ID,
                    nonce = getNonce(fromAddress).toInt(),
                    gasLimit = gas.gasLimit,
                    maxFeePerGas = gas.maxFeePerGas,
                    maxPriorityFeePerGas = gas.maxPriorityFeePerGas,
                    gasPriceInWei = null,
                    actionSource = null
                ).action()

                val txHash = suspendCancellableCoroutine<String?> { continuation ->
                    client.makeRequest(RequestContent.Request(listOf(action))) { result ->
                        result.onSuccess { actions ->
                            val hash = (actions.firstOrNull() as? ActionResult.Result)?.value
                            continuation.resume(hash)
                        }
                        result.onFailure {
                            continuation.resume(null)
                        }
                    }
                }

                if (txHash == null) {
                    onMintFailure.invoke(DialogContent(title = "Mint process error", text = "Approval declined by user."))
                    return@withContext false
                }

                if (!waitForReceipt(txHash)) {
                    onMintFailure.invoke(DialogContent(title = "Mint process error", text = "Approval transaction failed on the blockchain."))
                    return@withContext false
                }
            }

            // 4. Final Minting
            return@withContext executeMint(client, listingId, fromAddress, onMintFailure)

        } catch (e: Exception) {
            onMintFailure.invoke(DialogContent(title = "Mint process error", text = e.message ?: "Unknown error"))
            false
        }
    }

    suspend fun executeMint(
        client: CoinbaseWalletSDK,
        listingId: String,
        fromAddress: String,
        onMintFailure: (DialogContent) -> Unit
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val mintData = encodeBuyAutoURIData(BigInteger(listingId), fromAddress)
            val gas = calculateGasEstimate(fromAddress, MARKETPLACE_ADDRESS, mintData)

            val action = Web3JsonRPC.SendTransaction(
                fromAddress = fromAddress,
                toAddress = MARKETPLACE_ADDRESS,
                weiValue = "0",
                data = mintData,
                chainId = CHAIN_ID,
                nonce = getNonce(fromAddress).toInt(),
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
                        if (res != null) {
                            onMintFailure.invoke(DialogContent(title = "Success", text = "Transaction sent successfully!"))
                            continuation.resume(true)
                        } else {
                            onMintFailure.invoke(DialogContent(title = "Mint process error", text = "Transaction was not completed."))
                            continuation.resume(false)
                        }
                    }
                    result.onFailure {
                        onMintFailure.invoke(DialogContent(title = "Mint process error", text = it.message ?: "Transaction declined or failed"))
                        continuation.resume(false)
                    }
                }
            }
        } catch (e: Exception) {
            onMintFailure.invoke(DialogContent(title = "Mint process error", text = e.message ?: "Unknown error"))
            false
        }
    }
}