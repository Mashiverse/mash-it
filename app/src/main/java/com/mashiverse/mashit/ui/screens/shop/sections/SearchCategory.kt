package com.mashiverse.mashit.ui.screens.shop.sections

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.mashiverse.mashit.data.states.intents.ImageIntent
import com.mashiverse.mashit.data.models.nft.Nft
import com.mashiverse.mashit.ui.screens.shop.items.SectionLoading
import com.mashiverse.mashit.ui.screens.shop.items.SectionRefresh
import com.mashiverse.mashit.ui.screens.shop.items.ShopItem
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.Padding
import com.mashiverse.mashit.utils.helpers.detectScreenType
import com.mashiverse.mashit.utils.helpers.getItemWidthAndHeight

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun SearchCategory(
    items: LazyPagingItems<Nft>,
    selectId: (String) -> Unit,
    processImageIntent: (ImageIntent) -> Unit,
    getSoldQty: (Int, (Int) -> Unit) -> Unit,
    onMint: (String, Double, Boolean) -> Unit,
    onSearchClear: () -> Unit
) {
    val config = LocalConfiguration.current
    val screenType = config.detectScreenType()
    val (width, height) = config.getItemWidthAndHeight(screenType.shopColumns)

    val appendState = items.loadState.append

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Search",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = ContentAccentColor
            )

            Spacer(modifier = Modifier.weight(1F))

            TextButton(
                onClick = {
                    onSearchClear.invoke()
                }
            ) {
                Text(
                    text = "Clear",
                    fontSize = 16.sp,
                    color = ContentColor,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(screenType.shopColumns),
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(Padding)
        ) {
            items(
                count = items.itemCount,
                key = items.itemKey { it.name }
            ) { index ->
                val nft = items[index]
                nft?.let {
                    ShopItem(
                        nft = nft,
                        selectId = selectId,
                        processImageIntent = processImageIntent,
                        getSoldQty = getSoldQty,
                        onMint = onMint,
                        imageWidth = width,
                        imageHeight = height
                    )
                }
            }

            if (appendState is LoadState.Loading) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    SectionLoading()
                }
            }

            if (appendState is LoadState.Error) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    SectionRefresh(
                        onRetry = { items.retry() }
                    )
                }
            }
        }
    }
}