package com.mashiverse.mashit.ui.screens.artists.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import com.mashiverse.mashit.nav.routes.MainRoutes
import com.mashiverse.mashit.ui.screens.artists.ProfilePicture
import com.mashiverse.mashit.ui.screens.shop.items.SectionLoading
import com.mashiverse.mashit.ui.screens.shop.items.SectionRefresh
import com.mashiverse.mashit.ui.screens.shop.items.ShopItem
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.Padding
import com.mashiverse.mashit.utils.helpers.detectScreenType
import com.mashiverse.mashit.utils.helpers.getItemWidthAndHeight

@Composable
fun ArtistPage(alias: String, parentNavController: NavHostController) {
    val config = LocalConfiguration.current
    val screenType = config.detectScreenType()
    val (width, height) = config.getItemWidthAndHeight(screenType.shopColumns)

    val viewModel = hiltViewModel<ArtistPageViewModel>()
    val pageInfo by viewModel.pageInfo

    // FIX 1: Remember the Paging Flow based on the alias.
    // This prevents creating a new Pager every time the UI recomposes.
    val listingsFlow = remember(alias) {
        viewModel.getListingsPagingData(alias)
    }
    val listings = listingsFlow.collectAsLazyPagingItems()

    val appendState = listings.loadState.append

    LaunchedEffect(alias) {
        viewModel.fetchArtistPage(alias)
    }

    // FIX 2: Remember the callback to prevent the lambda from being recreated.
    val getSoldQty = remember(viewModel) {
        { listingId: Int, callback: (Int) -> Unit ->
            viewModel.getTotalSold(listingId, callback)
        }
    }

    Column {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth(),
            model = pageInfo?.bannerUrl,
            contentDescription = null
        )

        pageInfo?.let { info ->
            Column {
                Row {
                    Spacer(modifier = Modifier.width(16.dp))
                    ProfilePicture(
                        modifier = Modifier.offset(y = if (info.bannerUrl.isNotEmpty()) (-40).dp else 0.dp),
                        onClick = {},
                        artistMashup = info.mashup,
                        processImageIntent = { intent -> viewModel.processImageIntent(intent) }
                    )
                }

                Text(
                    text = info.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = ContentAccentColor
                )

                Text(
                    text = info.bio,
                    fontSize = 14.sp,
                    color = ContentColor
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


        // FIX 3: Unified Scrolling.
        // We move the Header inside the Grid to avoid Column + Grid conflict.
        LazyVerticalGrid(
            columns = GridCells.Fixed(screenType.shopColumns),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(Padding)
        ) {

            // --- Grid Items ---
            items(
                count = listings.itemCount,
                // FIX 4: Use a unique ID (if available) or the index.
                // 'it.name' can cause issues if two items share a name.
                key = listings.itemKey { it.name }
            ) { index ->
                val nft = listings[index]
                nft?.let {
                    ShopItem(
                        nft = nft,
                        selectId = { },
                        processImageIntent = { intent -> viewModel.processImageIntent(intent) },
                        getSoldQty = getSoldQty,
                        onMint = { _, _, _ ->
                            parentNavController.navigate(route = MainRoutes.Shop(nft.productInfo?.id))
                        },
                        imageWidth = width,
                        imageHeight = height
                    )
                }
            }

            // --- States ---
            if (appendState is LoadState.Loading) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    SectionLoading()
                }
            }

            if (appendState is LoadState.Error) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    SectionRefresh(onRetry = { listings.retry() })
                }
            }
        }
    }
}