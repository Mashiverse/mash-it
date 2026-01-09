import com.reown.appkit.client.AppKit
import com.reown.appkit.client.models.request.Request
import com.reown.appkit.client.models.request.SentRequestResult
import timber.log.Timber
import java.net.UnknownHostException

fun sendBasicTransactionSafe(
    recipientAddress: String,
    valueInEth: Double = 1.0,
    retryCount: Int = 3
) {
    val account = AppKit.getAccount()
    val session = AppKit.getSession() // Check if a session exists

    if (account == null) {
        Timber.tag("WEB3_CHECK").e("No wallet connected! User must approve session first.")
        return
    }

    if (session == null) {
        Timber.tag("WEB3_CHECK").w("No active WalletConnect session. Cannot send transaction.")
        return
    }

    if (!recipientAddress.matches(Regex("^0x[a-fA-F0-9]{40}$"))) {
        Timber.tag("WEB3_CHECK").e("Invalid recipient address format: $recipientAddress")
        return
    }

    val weiValue = (valueInEth * 1_000_000_000_000_000_000).toLong()
    val valueHex = "0x${weiValue.toString(16)}"

    Timber.tag("WEB3_CHECK").d("=== Transaction Details ===")
    Timber.tag("WEB3_CHECK").d("From: ${account.address}")
    Timber.tag("WEB3_CHECK").d("To: $recipientAddress")
    Timber.tag("WEB3_CHECK").d("Value: $valueInEth ETH ($valueHex Wei)")
    Timber.tag("WEB3_CHECK").d("Chain: ${account.chain}")

    val requestParams = """
        [{
            "from": "${account.address}",
            "to": "$recipientAddress",
            "value": "$valueHex",
            "data": "0x"
        }]
    """.trimIndent()

    fun attemptRequest(attempt: Int) {
        try {
            AppKit.request(
                request = Request(
                    method = "eth_sendTransaction",
                    params = requestParams
                ),
                onSuccess = { result: SentRequestResult ->
                    val txHash = result.toString() ?: "Unknown"
                    Timber.tag("WEB3_CHECK").d("✅ Transaction queued! TX Hash: $txHash")
                },
                onError = { error ->
                    when (error) {
                        is UnknownHostException -> {
                            if (attempt < retryCount) {
                                Timber.tag("WEB3_CHECK").w("⚠️ Network error, retrying... ($attempt/$retryCount)")
                                attemptRequest(attempt + 1)
                            } else {
                                Timber.tag("WEB3_CHECK").e("❌ Transaction failed due to network error: ${error.message}")
                            }
                        }
                        else -> {
                            Timber.tag("WEB3_CHECK").e("❌ Transaction failed: ${error.message}")
                            error.printStackTrace()
                        }
                    }
                }
            )
        } catch (e: Throwable) {
            Timber.tag("WEB3_CHECK").e(e, "🚨 SDK internal error while sending transaction")
        }
    }

    attemptRequest(1)
}
