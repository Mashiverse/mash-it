package com.mashiverse.mashit.utils.helpers.web3

import com.coinbase.android.nativesdk.CoinbaseWalletSDK
import com.coinbase.android.nativesdk.message.request.Action
import com.coinbase.android.nativesdk.message.request.RequestContent
import com.coinbase.android.nativesdk.message.request.Web3JsonRPC
import com.coinbase.android.nativesdk.message.response.ActionResult
import com.mashiverse.mashit.BuildConfig
import com.mashiverse.mashit.data.models.sys.dialog.DialogContent
import com.mashiverse.mashit.data.models.sys.wallet.WalletType
import com.reown.appkit.client.AppKit
import com.reown.appkit.client.models.request.Request
import com.reown.appkit.client.models.request.SentRequestResult
import com.reown.appkit.engine.coinbase.CoinbaseResult
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
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
    private const val MARKETPLACE_ADDRESS = "0x69945bc1F0fa219d3b9063B62EB2ED6f99e3EF09"
    private const val USDC_ADDRESS = "0x3c499c542cEF5E3811e1192ce70d8cC03d5c3359"
    private const val CHAIN_ID_APPKIT = "eip155:137"
    private const val CHAIN_ID_RAW = "137"

    private val MAX_UINT256 = BigInteger("f".repeat(64), 16)
    private val web3j =
        Web3j.build(HttpService("https://polygon-mainnet.g.alchemy.com/v2/${BuildConfig.ALCHEMY_API_KEY}"))

    // --- Public API ---

    suspend fun mint(
        walletType: WalletType,
        client: CoinbaseWalletSDK?,
        fromAddress: String,
        listingId: String,
        price: Double,
        onDialogTrigger: (DialogContent) -> Unit // Restored for validation feedback
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val lid = BigInteger(listingId)
            val usdcPrice = (price * 1_000_000).toLong().toBigInteger()

            // 1. Balance Check
            if (getUsdcBalance(fromAddress = fromAddress) < usdcPrice) {
                withContext(Dispatchers.Main) {
                    onDialogTrigger(
                        DialogContent(
                            title = "Insufficient balance",
                            text = "Please top up USDC"
                        )
                    )
                }
                return@withContext false
            }

            // 2. Limit Check
            if (!canUserMint(listingId = lid, userAddress = fromAddress)) {
                withContext(Dispatchers.Main) {
                    onDialogTrigger(
                        DialogContent(
                            title = "Limit Reached",
                            text = "You've reached the limit"
                        )
                    )
                }
                return@withContext false
            }

            // Transaction Execution (No internal dialogs here)
            return@withContext when (walletType) {
                WalletType.BASE -> if (client != null) performBaseMint(
                    client = client,
                    from = fromAddress,
                    lid = lid,
                    usdcPrice = usdcPrice
                ) else true

                WalletType.MM -> performMetamaskMint(
                    from = fromAddress,
                    lid = lid,
                    usdcPrice = usdcPrice
                )
            }
        } catch (e: Exception) {
            Timber.e(e)
            false
        }
    }

    // --- Coinbase Path ---

    private suspend fun performBaseMint(
        client: CoinbaseWalletSDK,
        from: String,
        lid: BigInteger,
        usdcPrice: BigInteger
    ): Boolean = withContext(Dispatchers.IO) {
        val actions = mutableListOf<Action>()
        val nonce = fetchNonce(address = from)
        var currentNonce = nonce

        if (getUsdcAllowanceSync(owner = from) < usdcPrice) {
            actions.add(
                buildBaseTx(
                    from = from,
                    to = USDC_ADDRESS,
                    data = encodeERC20Approve(spender = MARKETPLACE_ADDRESS, amount = MAX_UINT256),
                    nonce = currentNonce
                )
            )
            currentNonce++
        }
        actions.add(
            buildBaseTx(
                from = from,
                to = MARKETPLACE_ADDRESS,
                data = encodeBuyAutoURIData(id = lid, recipient = from),
                nonce = currentNonce
            )
        )

        withTimeoutOrNull(60000L) {
            suspendCancellableCoroutine { continuation ->
                client.makeRequest(RequestContent.Request(actions = actions)) { result ->
                    result.onSuccess { results ->
                        val success =
                            results.isNotEmpty() && results.all { it is ActionResult.Result }
                        continuation.resumeSafely(value = success)
                    }
                    result.onFailure { continuation.resumeSafely(value = false) }
                }
            }
        } ?: false
    }

    // --- MetaMask Path ---

    private suspend fun performMetamaskMint(
        from: String,
        lid: BigInteger,
        usdcPrice: BigInteger
    ): Boolean {
        // Step 1: Approval
        if (getUsdcAllowanceSync(owner = from) < usdcPrice) {
            val approveHash = requestAppKitTx(
                from = from,
                to = USDC_ADDRESS,
                data = encodeERC20Approve(spender = MARKETPLACE_ADDRESS, amount = MAX_UINT256)
            )
            if (approveHash == null) return false
            waitForTransaction(txHash = approveHash)
        }

        // Step 2: Minting
        val mintHash = requestAppKitTx(
            from = from,
            to = MARKETPLACE_ADDRESS,
            data = encodeBuyAutoURIData(id = lid, recipient = from)
        )
        return mintHash != null
    }

    private suspend fun requestAppKitTx(from: String, to: String, data: String): String? =
        suspendCancellableCoroutine { continuation ->
            val req = Request(
                method = "eth_sendTransaction",
                params = "[{\"from\":\"$from\",\"to\":\"$to\",\"data\":\"$data\",\"value\":\"0x0\"}]",
                chainId = CHAIN_ID_APPKIT
            )
            AppKit.request(
                request = req,
                onSuccess = { res: SentRequestResult ->
                    val hash = when (res) {
                        is SentRequestResult.WalletConnect -> "pending_metamask"
                        is SentRequestResult.Coinbase -> (res.results.firstOrNull() as? CoinbaseResult.Result)?.value
                    }
                    continuation.resumeSafely(value = hash)
                },
                onError = { continuation.resumeSafely(value = null) }
            )
        }

    // --- Helpers ---

    private fun buildBaseTx(from: String, to: String, data: String, nonce: Int) =
        Web3JsonRPC.SendTransaction(
            fromAddress = from, toAddress = to, weiValue = "0", data = data,
            chainId = CHAIN_ID_RAW, nonce = nonce, gasLimit = "350000",
            maxFeePerGas = "500000000000", maxPriorityFeePerGas = "50000000000",
            gasPriceInWei = null, actionSource = null
        ).action()

    private fun <T> CancellableContinuation<T>.resumeSafely(value: T) {
        if (isActive) resume(value)
    }

    private suspend fun waitForTransaction(txHash: String) {
        if (txHash == "pending_metamask") {
            delay(4500); return
        }
        repeat(10) {
            val receipt = try {
                web3j.ethGetTransactionReceipt(txHash).send().transactionReceipt
            } catch (e: Exception) {
                java.util.Optional.empty()
            }
            if (receipt.isPresent) return@repeat
            delay(2000)
        }
    }

    // --- Queries ---

    suspend fun getUsdcBalance(fromAddress: String): BigInteger = withContext(Dispatchers.IO) {
        queryContract(
            from = fromAddress,
            contract = USDC_ADDRESS,
            method = "balanceOf",
            params = listOf(Address(fromAddress))
        )
    }

    private fun getUsdcAllowanceSync(owner: String): BigInteger {
        return queryContract(
            from = owner,
            contract = USDC_ADDRESS,
            method = "allowance",
            params = listOf(Address(owner), Address(MARKETPLACE_ADDRESS))
        )
    }

    private fun queryContract(
        from: String,
        contract: String,
        method: String,
        params: List<org.web3j.abi.datatypes.Type<*>>
    ): BigInteger {
        return try {
            val fn = Function(method, params, listOf(object : TypeReference<Uint256>() {}))
            val res = web3j.ethCall(
                Transaction.createEthCallTransaction(
                    from,
                    contract,
                    FunctionEncoder.encode(fn)
                ), DefaultBlockParameterName.LATEST
            ).send()
            val decoded = FunctionReturnDecoder.decode(res.value, fn.outputParameters)
            if (decoded.isNotEmpty()) decoded[0].value as BigInteger else BigInteger.ZERO
        } catch (e: Exception) {
            BigInteger.ZERO
        }
    }

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

    private fun fetchNonce(address: String) =
        web3j.ethGetTransactionCount(address, DefaultBlockParameterName.PENDING)
            .send().transactionCount.toInt()

    private fun encodeBuyAutoURIData(id: BigInteger, recipient: String) = FunctionEncoder.encode(
        Function(
            "buyAutoURI",
            listOf(Uint256(id), Uint256(BigInteger.ONE), Address(recipient)),
            emptyList()
        )
    )

    private fun encodeERC20Approve(spender: String, amount: BigInteger) = FunctionEncoder.encode(
        Function(
            "approve",
            listOf(Address(spender), Uint256(amount)),
            emptyList()
        )
    )
}