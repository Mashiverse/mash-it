package com.mashiverse.mashit.ui.screens.shop.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.coinbase.android.nativesdk.CoinbaseWalletSDK
import com.mashiverse.mashit.data.intents.ImageIntent
import com.mashiverse.mashit.data.intents.ShopIntent
import com.mashiverse.mashit.data.intents.Web3Intent
import com.mashiverse.mashit.data.states.ShopUiState
import com.mashiverse.mashit.ui.indicators.LoadingIndicator
import com.mashiverse.mashit.ui.screens.shop.items.SectionLoading
import com.mashiverse.mashit.ui.screens.shop.items.SectionRefresh
import com.mashiverse.mashit.ui.screens.shop.items.ShopItem
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.Padding
import com.mashiverse.mashit.ui.theme.Secondary
import com.mashiverse.mashit.utils.helpers.sys.detectScreenType
import com.mashiverse.mashit.utils.helpers.sys.getItemWidthAndHeight
import java.util.Locale

@Composable
fun Category(
    shopUiState: ShopUiState,
    clientRef: CoinbaseWalletSDK,
    processImageIntent: (ImageIntent) -> Unit,
    onSearchQueryClear: (() -> Unit)? = null,
    processShopIntent: (ShopIntent) -> Unit,
    processWeb3Intent: (Web3Intent) -> Unit,
) {
    val isSearch = onSearchQueryClear != null

    val items = shopUiState.itemsData.collectAsLazyPagingItems()

    val config = LocalConfiguration.current
    val screenType = config.detectScreenType()
    val (width, height) = config.getItemWidthAndHeight(screenType.shopColumns)

    val appendState = items.loadState.append
    val refreshState = items.loadState.refresh

    Box {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = shopUiState.category.name.lowercase()
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                    fontSize = 14.sp,
                    color = ContentAccentColor
                )

                Spacer(modifier = Modifier.weight(1F))

                if (isSearch) {
                    TextButton(
                        modifier = Modifier.height(32.dp),
                        onClick = onSearchQueryClear
                    ) {
                        Text(
                            text = "Dismiss",
                            fontSize = 12.sp,
                            color = ContentColor,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(screenType.shopColumns),
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(Padding)
            ) {
                items(
                    count = items.itemCount,
                    key = items.itemKey { it.compositeUrl }
                ) { index ->
                    val nft = items[index]
                    nft?.let {
                        ShopItem(
                            nft = nft,
                            processShopIntent = processShopIntent,
                            processImageIntent = processImageIntent,
                            clientRef = clientRef,
                            processWeb3Intent = processWeb3Intent,
                            imageWidth = width,
                            imageHeight = height
                        )
                    }
                }

                if (isSearch && refreshState is LoadState.NotLoading && items.itemCount == 0) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(32.dp)
                                .clip(RoundedCornerShape(24))
                                .background(Secondary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Nothing found",
                                fontSize = 14.sp,
                                color = ContentColor
                            )
                        }
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

        if (!isSearch && refreshState is LoadState.Loading) {
            LoadingIndicator(text = "Loading")
        }

        if (isSearch && refreshState is LoadState.Loading) {
            LoadingIndicator(text = "Searching")
        }
    }
}