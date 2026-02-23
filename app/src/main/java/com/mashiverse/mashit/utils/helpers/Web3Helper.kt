package com.mashiverse.mashit.utils.helpers

import com.coinbase.android.nativesdk.CoinbaseWalletSDK
import com.coinbase.android.nativesdk.message.request.Action
import com.coinbase.android.nativesdk.message.request.RequestContent
import com.coinbase.android.nativesdk.message.request.Web3JsonRPC
import com.coinbase.android.nativesdk.message.response.ActionResult
import com.mashiverse.mashit.BuildConfig
import com.mashiverse.mashit.data.models.dialog.DialogContent
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
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.coroutines.resume

object Web3Helper {
    private const val MARKETPLACE_ADDRESS = "0x69945bc1F0fa219d3b9063B62EB2ED6f99e3EF09"
    private const val USDC_ADDRESS = "0x3c499c542cEF5E3811e1192ce70d8cC03d5c3359"
    private const val CHAIN_ID = "137"

    private val MAX_UINT256 = BigInteger("f".repeat(64), 16)
    private val web3j = Web3j.build(HttpService("https://polygon-mainnet.g.alchemy.com/v2/${BuildConfig.ALCHEMY_API_KEY}"))

    // --- Coinbase SDK Coroutine Bridge ---

    private suspend fun CoinbaseWalletSDK.requestAction(action: Action): String? =
        suspendCancellableCoroutine { continuation ->
            this.makeRequest(RequestContent.Request(listOf(action))) { result ->
                result.onSuccess { actions ->
                    val hash = (actions.firstOrNull() as? ActionResult.Result)?.value
                    if (continuation.isActive) continuation.resume(hash)
                }
                result.onFailure {
                    if (continuation.isActive) continuation.resume(null)
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
                val transaction = Transaction.createEthCallTransaction(userAddress, MARKETPLACE_ADDRESS, mintData)
                val estimateResponse = web3j.ethEstimateGas(transaction).send()

                if (estimateResponse.hasError()) {
                    val errorMessage = estimateResponse.error.message.lowercase()
                    // Revert usually means the contract logic (limit, supply, etc.) blocked it
                    if (errorMessage.contains("revert")) return@withContext false
                }
                true
            } catch (e: Exception) {
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
                Transaction.createEthCallTransaction(address, USDC_ADDRESS, FunctionEncoder.encode(function)),
                DefaultBlockParameterName.LATEST
            ).send()
            val results = FunctionReturnDecoder.decode(response.value, function.outputParameters)
            if (results.isNotEmpty()) results[0].value as BigInteger else BigInteger.ZERO
        } catch (e: Exception) {
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
                Transaction.createEthCallTransaction(owner, USDC_ADDRESS, FunctionEncoder.encode(function)),
                DefaultBlockParameterName.LATEST
            ).send()
            val results = FunctionReturnDecoder.decode(response.value, function.outputParameters)
            if (results.isNotEmpty()) results[0].value as BigInteger else BigInteger.ZERO
        } catch (e: Exception) {
            BigInteger.ZERO
        }
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
            val lid = BigInteger(listingId)

            // Use BigDecimal for safe currency multiplication
            val usdcPrice = BigDecimal.valueOf(price)
                .multiply(BigDecimal.valueOf(1_000_000))
                .toBigInteger()

            if (getUsdcBalance(fromAddress) < usdcPrice) {
                onMintFailure.invoke(DialogContent(title = "Insufficient balance", text = "Please top up the wallet to continue"))
                return@withContext false
            }

            // Check if we actually need to approve before prompting the user
            val currentAllowance = getUsdcAllowance(fromAddress)
            if (currentAllowance < usdcPrice) {
                val approveData = encodeERC20Approve(MARKETPLACE_ADDRESS, MAX_UINT256)

                // Set gas/nonce to null to let Coinbase Wallet handle gas spikes automatically
                val action = Web3JsonRPC.SendTransaction(
                    fromAddress = fromAddress,
                    toAddress = USDC_ADDRESS,
                    weiValue = "0",
                    data = approveData,
                    chainId = CHAIN_ID,
                    nonce = null,
                    gasLimit = null,
                    maxFeePerGas = null,
                    maxPriorityFeePerGas = null,
                    gasPriceInWei = null,
                    actionSource = null
                ).action()

                val txHash = client.requestAction(action)
                if (txHash == null || !waitForReceipt(txHash)) {
                    onMintFailure.invoke(DialogContent(title = "Process Error", text = "Approval failed or was rejected"))
                    return@withContext false
                }
            }

            // Final check: can the contract logic execute?
            if (!canUserMint(lid, fromAddress)) {
                onMintFailure.invoke(DialogContent(title = "Limit Reached", text = "You've reached the limit for the listing"))
                return@withContext false
            }

            return@withContext executeMint(client, listingId, fromAddress, onMintFailure)

        } catch (e: Exception) {
            onMintFailure.invoke(DialogContent(title = "Process Error", text = "Something went wrong"))
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

            val action = Web3JsonRPC.SendTransaction(
                fromAddress = fromAddress,
                toAddress = MARKETPLACE_ADDRESS,
                weiValue = "0",
                data = mintData,
                chainId = CHAIN_ID,
                nonce = null,
                gasLimit = null,
                maxFeePerGas = null,
                maxPriorityFeePerGas = null,
                gasPriceInWei = null,
                actionSource = null
            ).action()

            val txHash = client.requestAction(action)

            if (txHash != null) {
                onMintFailure.invoke(DialogContent(title = "Success!", text = "You've just minted new Mashi!"))
                true
            } else {
                onMintFailure.invoke(DialogContent(title = "Process Error", text = "Something went wrong"))
                false
            }
        } catch (e: Exception) {
            onMintFailure.invoke(DialogContent(title = "Process Error", text = "Something went wrong"))
            false
        }
    }
}