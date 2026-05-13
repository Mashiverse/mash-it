package com.mashiverse.mashit.ui.screens.shop.sections

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.coinbase.android.nativesdk.CoinbaseWalletSDK
import com.mashiverse.mashit.data.states.shop.ShopIntent
import com.mashiverse.mashit.data.states.shop.ShopUiState
import com.mashiverse.mashit.data.states.sys.ImageIntent
import com.mashiverse.mashit.data.states.web3.Web3Intent
import com.mashiverse.mashit.ui.default.grids.ShopItemGrid
import com.mashiverse.mashit.ui.default.indicators.LoadingIndicator
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.MediumPadding
import com.mashiverse.mashit.ui.theme.Padding
import com.mashiverse.mashit.utils.helpers.sys.detectScreenType
import com.mashiverse.mashit.utils.helpers.sys.filter
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun Category(
    shopUiState: ShopUiState,
    clientRef: CoinbaseWalletSDK?,
    processImageIntent: (ImageIntent) -> Unit,
    onSearchQueryClear: (() -> Unit)? = null,
    processShopIntent: (ShopIntent) -> Unit,
    processWeb3Intent: (Web3Intent) -> Unit,
    categoryState: LazyGridState,
) {
    val isSearch = onSearchQueryClear != null

    val data = shopUiState.itemsData
    var isAvailableOnly by remember(data) { mutableStateOf(false) }

    val items = remember(isAvailableOnly, data) {
        data.filter(isAvailableOnly)
    }.collectAsLazyPagingItems()


    val config = LocalConfiguration.current
    val screenType = config.detectScreenType()


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
                Checkbox(
                    checked = isAvailableOnly,
                    onCheckedChange = {
                        isAvailableOnly = !isAvailableOnly
                    }
                )

                Text(
                    text = "Available?",
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

            Spacer(modifier = Modifier.height(MediumPadding))

            ShopItemGrid(
                items = items,
                appendState = appendState,
                state = categoryState,
                columns = screenType.shopColumns,
                spacedByHoriz = MediumPadding,
                spacedByVert = Padding,
                isRefreshing = refreshState is LoadState.NotLoading,
                isSearch = isSearch,
                processShopIntent = processShopIntent,
                clientRef = clientRef,
                processImageIntent = processImageIntent,
                processWeb3Intent = processWeb3Intent,
            )

        }

        if (!isSearch && refreshState is LoadState.Loading) {
            LoadingIndicator(text = "Loading")
        }

        if (isSearch && refreshState is LoadState.Loading) {
            LoadingIndicator(text = "Searching")
        }
    }
}
