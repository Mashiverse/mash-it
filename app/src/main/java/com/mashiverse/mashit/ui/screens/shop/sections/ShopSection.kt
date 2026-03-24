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
import androidx.paging.compose.LazyPagingItems
import com.mashiverse.mashit.data.states.intents.ImageIntent
import com.mashiverse.mashit.data.models.nft.Nft
import com.mashiverse.mashit.data.models.nft.PriceCurrency
import com.mashiverse.mashit.ui.screens.shop.items.ShopItem
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.SmallPadding

@Composable
fun ShopSection(
    sectionName: String,
    selectId: (String) -> Unit,
    sectionItems: LazyPagingItems<Nft>,
    processImageIntent: (ImageIntent) -> Unit,
    onCategorySelect: (String, LazyPagingItems<Nft>) -> Unit, // TODO: Category select
    getSoldQty: (Int, (Int) -> Unit) -> Unit,
    onMint: (String, Double, Boolean) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = sectionName,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = ContentAccentColor
            )

            Spacer(modifier = Modifier.weight(1F))

            TextButton(
                onClick = {
                    onCategorySelect.invoke(sectionName, sectionItems)
                }
            ) {
                Text(
                    text = "See all",
                    fontSize = 14.sp,
                    color = ContentColor,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(SmallPadding)
        ) {
            val itemCount = minOf(sectionItems.itemCount, 20)
            items(count = itemCount) { index ->
                val nft = sectionItems[index]

                if (nft != null && nft.productInfo?.priceCurrency != PriceCurrency.POL) {
                    ShopItem(
                        nft = nft,
                        selectId = selectId,
                        processImageIntent = processImageIntent,
                        getSoldQty = getSoldQty,
                        onMint = onMint
                    )
                }
            }
        }
    }
}