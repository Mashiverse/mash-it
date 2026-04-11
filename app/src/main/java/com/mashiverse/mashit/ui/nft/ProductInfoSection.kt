package com.mashiverse.mashit.ui.nft

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.coinbase.android.nativesdk.CoinbaseWalletSDK
import com.mashiverse.mashit.data.intents.Web3Intent
import com.mashiverse.mashit.data.models.nft.Nft
import com.mashiverse.mashit.data.models.nft.PriceCurrency
import com.mashiverse.mashit.ui.buttons.BuyButton
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.ExtraSmallPadding
import com.mashiverse.mashit.ui.theme.Padding

@Composable
fun ProductInfoSection(
    nft: Nft,
    processWeb3Intent: (Web3Intent) -> Unit,
    clientRef: CoinbaseWalletSDK
) {
    val productInfo = nft.productInfo!!

    var soldQty by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(Unit) {
        processWeb3Intent.invoke(
            Web3Intent.OnTotalSoldGet(
                nft.productInfo.listingId.toInt()
            ) { v ->
                soldQty = v
            }
        )
    }

    Text(
        text = "${productInfo.price.toInt()} ${productInfo.priceCurrency.name}",
        color = ContentAccentColor,
        fontSize = 14.sp
    )

    Spacer(modifier = Modifier.height(Padding))

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Max: ${productInfo.perWallet}",
                color = ContentColor,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(ExtraSmallPadding))

            Text(
                text = "$soldQty/${productInfo.quantity} sold",
                color = ContentColor,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.weight(1F))

        BuyButton(
            text = when {
                productInfo.delisted -> "Delisted"
                soldQty < productInfo.quantity -> "Buy"
                else -> "Sold"
            },
            enabled = when {
                productInfo.delisted -> false
                soldQty < productInfo.quantity -> true
                else -> false
            },
            textSize = 14.sp,
            onClick = {
                processWeb3Intent.invoke(
                    Web3Intent.OnMint(
                        client = clientRef,
                        listingId = nft.productInfo.listingId,
                        price = nft.productInfo.price,
                        isPolCurrency = nft.productInfo.priceCurrency == PriceCurrency.POL
                    )
                )
            }
        )
    }
}