package com.mashiverse.mashit.ui.screens.mashi

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mashiverse.mashit.data.models.mashi.NftDetails
import com.mashiverse.mashit.data.models.mashi.ProductInfo
import com.mashiverse.mashit.ui.screens.buttons.BuyButton
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.ExtraSmallPaddingSize
import com.mashiverse.mashit.ui.theme.PaddingSize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MashiProductInfoSection(
    listingDetails: NftDetails.ListingDetails,
    getSoldQty: (suspend (Int) -> Int)?,
    ) {
    val scope = rememberCoroutineScope()

    val isPaused = listingDetails.isPaused
    val productInfo = listingDetails.productInfo

    var soldQty by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(Unit) {
        scope.launch(Dispatchers.IO) {
            soldQty = getSoldQty?.invoke(listingDetails.listingId.toInt()) ?: 0
        }
    }

    Text(
        text = "${productInfo.price} ${productInfo.priceCurrency.name}",
        color = ContentAccentColor,
        fontSize = 14.sp
    )

    Spacer(modifier = Modifier.height(PaddingSize))

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Per-wallet: ${productInfo.perWallet}",
                color = ContentColor,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(ExtraSmallPaddingSize))

            Text(
                text = "$soldQty/${productInfo.quantity} sold",
                color = ContentColor,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.weight(1F))

        BuyButton(
            text = when {
                soldQty < productInfo.quantity -> "Buy"
                isPaused -> "Delisted"
                else -> "Sold"
            },
            height = 32.dp,
            width = 80.dp,
            enabled = when {
                soldQty < productInfo.quantity -> true
                else -> false
            },
            textSize = 16.sp
        )
    }
}