package com.mashiverse.mashit.utils.helpers

import com.coinbase.android.nativesdk.CoinbaseWalletSDK
import com.coinbase.android.nativesdk.message.request.RequestContent
import com.coinbase.android.nativesdk.message.request.Web3JsonRPC
import com.coinbase.android.nativesdk.message.response.ActionResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.generated.Uint256
import timber.log.Timber
import java.math.BigInteger

object Web3Helper {
    // 1. Updated to match the contract selector: buyAutoURI(uint256,uint256,address)
    fun encodeBuyAutoURIData(listingId: BigInteger, qty: BigInteger, recipient: String): String {
        val fn = org.web3j.abi.datatypes.Function(
            "buyAutoURI",
            listOf(
                Uint256(listingId),
                Uint256(qty),
                Address(recipient)
            ),
            emptyList()
        )
        return FunctionEncoder.encode(fn)
    }

    // 2. Standard ERC20 Approve Helper
    fun encodeERC20Approve(spender: String, amount: BigInteger): String {
        val fn = Function(
            "approve",
            listOf(Address(spender), Uint256(amount)),
            emptyList()
        )
        return FunctionEncoder.encode(fn)
    }

    fun preAuthorizeUsdc(
        client: CoinbaseWalletSDK,
        fromAddress: String = "0xd659688366e5a5a6190409dcd4834b3a5b7c88ba",
        marketplaceAddress: String = "0x69945bc1F0fa219d3b9063B62EB2ED6f99e3EF09" // Updated to match JS chunk
    ) {
        try {
            // Native USDC on Polygon: 0x3c499c542cEF5E3811e1192ce70d8cC03d5c3359
            // 1,000,000 units = 1 USDC (assuming 6 decimals)
            val approveData = encodeERC20Approve(marketplaceAddress, BigInteger("20000000"))

            val action = Web3JsonRPC.SendTransaction(
                fromAddress = fromAddress,
                toAddress = "0x3c499c542cEF5E3811e1192ce70d8cC03d5c3359",
                weiValue = "0",
                data = approveData,
                chainId = "137",
                nonce = null,
                gasPriceInWei = "100000000",
                maxFeePerGas = "100000000",
                maxPriorityFeePerGas = "100000000",
                gasLimit = "100000000",
                actionSource = null
            ).action()


            client.makeRequest(RequestContent.Request(listOf(action))) { result ->
                result.onSuccess {
                    Timber.d("✅ Approval Successful. Waiting for Polygon sync...")

                    CoroutineScope(Dispatchers.Main).launch {
                        delay(3000)
                        executeMint(client, BigInteger("448"))
                    }
                }
                result.onFailure { e ->
                    Timber.e("❌ Approval Failed: ${e.message}")
                }
            }
        } catch (e: Exception) {
            Timber.e("Error: ${e.message}")
        }
    }

    fun executeMint(
        client: CoinbaseWalletSDK,
        listingId: BigInteger,
        qty: BigInteger = BigInteger("1"),
        recipient: String = "0xd659688366e5a5a6190409dcd4834b3a5b7c88ba",
        fromAddress: String = "0xd659688366e5a5a6190409dcd4834b3a5b7c88ba",
        marketplaceAddress: String = "0x69945bc1F0fa219d3b9063B62EB2ED6f99e3EF09" // Updated
    ) {
        val mintData = encodeBuyAutoURIData(listingId, qty, recipient)

        val action = Web3JsonRPC.SendTransaction(
            fromAddress = fromAddress,
            toAddress = marketplaceAddress,
            weiValue = "0",
            data = mintData,
            chainId = "137",
            gasLimit = "10000000",
            nonce = null,
            gasPriceInWei = "10000000",
            maxFeePerGas = "10000000",
            maxPriorityFeePerGas = "10000000",
            actionSource = null
        ).action()

        client.makeRequest(RequestContent.Request(listOf(action))) { result ->
            result.onSuccess { actions ->
                val res = actions.firstOrNull() as? ActionResult.Result
                Timber.d("✅ buyAutoURI Success! Hash: ${res?.value}")
            }
            result.onFailure { e ->
                Timber.e("❌ buyAutoURI Failed: ${e.message}")
            }
        }
    }
}