package com.mashiverse.mashit.utils.helpers.web3

import com.coinbase.android.nativesdk.CoinbaseWalletSDK
import com.coinbase.android.nativesdk.message.request.Action
import com.coinbase.android.nativesdk.message.request.RequestContent
import com.coinbase.android.nativesdk.message.request.Web3JsonRPC
import com.coinbase.android.nativesdk.message.response.ActionResult
import com.mashiverse.mashit.BuildConfig
import com.mashiverse.mashit.data.models.dialog.DialogContent
import com.mashiverse.mashit.data.models.minting.GasEstimate
import kotlinx.coroutines.Dispatchers
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
import java.math.BigInteger
import kotlin.coroutines.resume

object Web3Helper {
    private const val MARKETPLACE_ADDRESS = "0x69945bc1F0fa219d3b9063B62EB2ED6f99e3EF09"
    private const val USDC_ADDRESS = "0x3c499c542cEF5E3811e1192ce70d8cC03d5c3359"
    private const val CHAIN_ID = "137"

    private val MAX_UINT256 = BigInteger("f".repeat(64), 16)
    private val web3j =
        Web3j.build(HttpService("https://polygon-mainnet.g.alchemy.com/v2/${BuildConfig.ALCHEMY_API_KEY}"))

    // --- Coinbase SDK Batch Bridge ---

    private suspend fun CoinbaseWalletSDK.requestActions(actions: List<Action>): List<String?> =
        suspendCancellableCoroutine { continuation ->
            this.makeRequest(RequestContent.Request(actions)) { result ->
                result.onSuccess { actionResults ->
                    val hashes = actionResults.map { (it as? ActionResult.Result)?.value }
                    if (continuation.isActive) continuation.resume(hashes)
                }
                result.onFailure {
                    if (continuation.isActive) continuation.resume(emptyList())
                }
            }
        }

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

    private suspend fun canUserMint(listingId: BigInteger, userAddress: String): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val mintData = encodeBuyAutoURIData(listingId, userAddress)
                val transaction =
                    Transaction.createEthCallTransaction(userAddress, MARKETPLACE_ADDRESS, mintData)
                val estimateResponse = web3j.ethEstimateGas(transaction).send()

                if (estimateResponse.hasError()) {
                    val errorMessage = estimateResponse.error.message.lowercase()
                    if (errorMessage.contains("revert") || errorMessage.contains("limit") || errorMessage.contains(
                            "max"
                        )
                    ) {
                        return@withContext false
                    }
                }
                true
            } catch (_: Exception) {
                true
            }
        }

    suspend fun getUsdcBalance(address: String): BigInteger = withContext(Dispatchers.IO) {
        try {
            val function = Function(
                "balanceOf",
                listOf(Address(address)),
                listOf(object : TypeReference<Uint256>() {})
            )
            val response = web3j.ethCall(
                Transaction.createEthCallTransaction(
                    address,
                    USDC_ADDRESS,
                    FunctionEncoder.encode(function)
                ), DefaultBlockParameterName.LATEST
            ).send()
            val results = FunctionReturnDecoder.decode(response.value, function.outputParameters)
            if (results.isNotEmpty()) results[0].value as BigInteger else BigInteger.ZERO
        } catch (_: Exception) {
            BigInteger.ZERO
        }
    }

    suspend fun getUsdcAllowance(owner: String): BigInteger = withContext(Dispatchers.IO) {
        try {
            val function = Function(
                "allowance",
                listOf(Address(owner), Address(MARKETPLACE_ADDRESS)),
                listOf(object : TypeReference<Uint256>() {})
            )
            val response = web3j.ethCall(
                Transaction.createEthCallTransaction(
                    owner,
                    USDC_ADDRESS,
                    FunctionEncoder.encode(function)
                ), DefaultBlockParameterName.LATEST
            ).send()
            val results = FunctionReturnDecoder.decode(response.value, function.outputParameters)
            if (results.isNotEmpty()) results[0].value as BigInteger else BigInteger.ZERO
        } catch (_: Exception) {
            BigInteger.ZERO
        }
    }

    private suspend fun getNonce(fromAddress: String): BigInteger = withContext(Dispatchers.IO) {
        web3j.ethGetTransactionCount(fromAddress, DefaultBlockParameterName.PENDING)
            .send().transactionCount
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
            val priorityFee = web3j.ethMaxPriorityFeePerGas().send().maxPriorityFeePerGas.multiply(
                BigInteger.valueOf(120)
            ).divide(BigInteger.valueOf(100))
            val maxFee = baseFee.multiply(BigInteger.valueOf(2)).add(priorityFee)
            val estimateGas = web3j.ethEstimateGas(
                Transaction.createEthCallTransaction(
                    fromAddress,
                    toAddress,
                    data
                )
            ).send().amountUsed
            GasEstimate(
                estimateGas.multiply(BigInteger.valueOf(120)).divide(BigInteger.valueOf(100))
                    .toString(),
                maxFee.toString(),
                priorityFee.toString()
            )
        } catch (_: Exception) {
            GasEstimate("350000", "500000000000", "50000000000")
        }
    }

    // --- The Refactored Mint Action (Batched) ---

    suspend fun mint(
        client: CoinbaseWalletSDK,
        fromAddress: String,
        listingId: String,
        price: Double,
        onMintFailure: (DialogContent) -> Unit
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val lid = BigInteger(listingId)
            val usdcPrice = (price * 1_000_000).toLong().toBigInteger()

            // 1. Validations
            if (getUsdcBalance(fromAddress) < usdcPrice) {
                onMintFailure(
                    DialogContent(
                        title = "Insufficient balance",
                        text = "Please top up the wallet"
                    )
                )
                return@withContext false
            }

            if (!canUserMint(lid, fromAddress)) {
                onMintFailure(
                    DialogContent(
                        title = "Limit Reached",
                        text = "You've reached the limit"
                    )
                )
                return@withContext false
            }

            val actions = mutableListOf<Action>()
            var nonce = getNonce(fromAddress)

            // 2. Check Allowance and prepare Approve Action if needed
            if (getUsdcAllowance(fromAddress) < usdcPrice) {
                val approveData = encodeERC20Approve(MARKETPLACE_ADDRESS, MAX_UINT256)
                val gas = calculateGasEstimate(fromAddress, USDC_ADDRESS, approveData)
                actions.add(createTxAction(fromAddress, USDC_ADDRESS, approveData, gas, nonce))
                nonce = nonce.add(BigInteger.ONE)
            }

            // 3. Prepare Mint Action
            val mintData = encodeBuyAutoURIData(lid, fromAddress)
            val mintGas = calculateGasEstimate(fromAddress, MARKETPLACE_ADDRESS, mintData)
            actions.add(createTxAction(fromAddress, MARKETPLACE_ADDRESS, mintData, mintGas, nonce))

            // 4. Send Batch (Single Wallet Popup)
            val txHashes = client.requestActions(actions)

            return@withContext if (txHashes.isNotEmpty() && txHashes.all { it != null }) {
                onMintFailure(
                    DialogContent(
                        title = "Success!",
                        text = "You've just minted new Mashi!"
                    )
                )
                true
            } else {
                onMintFailure(DialogContent(title = "Process Error", text = "Something went wrong"))
                false
            }

        } catch (_: Exception) {
            onMintFailure(DialogContent(title = "Process Error", text = "Something went wrong"))
            false
        }
    }

    // --- Parameters Kept Identical to original Web3JsonRPC ---

    private fun createTxAction(
        from: String,
        to: String,
        data: String,
        gas: GasEstimate,
        nonce: BigInteger
    ): Action =
        Web3JsonRPC.SendTransaction(
            fromAddress = from,
            toAddress = to,
            weiValue = "0",
            data = data,
            chainId = CHAIN_ID,
            nonce = nonce.toInt(),
            gasLimit = gas.gasLimit,
            maxFeePerGas = gas.maxFeePerGas,
            maxPriorityFeePerGas = gas.maxPriorityFeePerGas,
            gasPriceInWei = null,
            actionSource = null
        ).action()
}