package com.mashiverse.mashit.ui.screens.shop.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import com.coinbase.android.nativesdk.CoinbaseWalletSDK
import com.mashiverse.mashit.data.intents.ImageIntent
import com.mashiverse.mashit.data.intents.ShopIntent
import com.mashiverse.mashit.data.intents.Web3Intent
import com.mashiverse.mashit.data.states.ShopUiState
import com.mashiverse.mashit.ui.screens.shop.items.ShopItem
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.Padding

@Composable
fun ShopSection(
    shopUiState: ShopUiState,
    clientRef: CoinbaseWalletSDK,
    processImageIntent: (ImageIntent) -> Unit,
    processShopIntent: (ShopIntent) -> Unit,
    processWeb3Intent: (Web3Intent) -> Unit
) {
    val sectionItems = shopUiState.itemsData.collectAsLazyPagingItems()

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = shopUiState.category.name.uppercase(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = ContentAccentColor
            )

            Spacer(modifier = Modifier.weight(1F))

            TextButton(
                onClick = {
                    processShopIntent(ShopIntent.OnCategorySelect)
                }
            ) {
                Text(
                    text = "See all",
                    fontSize = 16.sp,
                    color = ContentColor,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Padding)
        ) {
            val itemCount = minOf(sectionItems.itemCount, 20)
            items(count = itemCount) { index ->
                val nft = sectionItems[index]

                if (nft != null) {
                    ShopItem(
                        nft = nft,
                        processShopIntent = processShopIntent,
                        processImageIntent = processImageIntent,
                        processWeb3Intent = processWeb3Intent,
                        clientRef = clientRef
                    )
                }
            }
        }
    }
}