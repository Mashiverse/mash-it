package com.mashiverse.mashit.ui.screens.artists.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import com.mashiverse.mashit.data.models.ScreenInfo
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
    val density = LocalDensity.current

    val screenType = config.detectScreenType()
    val (width, height) = config.getItemWidthAndHeight(screenType.shopColumns)

    val viewModel = hiltViewModel<ArtistPageViewModel>()
    val pageInfo by viewModel.pageInfo

    val listingsFlow = remember(alias) {
        viewModel.getListingsPagingData(alias)
    }
    val listings = listingsFlow.collectAsLazyPagingItems()

    val appendState = listings.loadState.append

    LaunchedEffect(alias) {
        viewModel.fetchArtistPage(alias)
    }

    val getSoldQty = { listingId: Int, callback: (Int) -> Unit ->
        viewModel.getTotalSold(listingId, callback)
    }

    var bannerHeight by remember { mutableStateOf(0.dp) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        pageInfo?.let { info ->
            Column {
                Box{
                    val model = if (screenType == ScreenInfo.COMPACT) info.bannerUrl else info.desktopBannerUrl

                    AsyncImage(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .fillMaxWidth()
                            .onSizeChanged { size ->
                                with(density) {
                                    bannerHeight = size.height.toDp()
                                }
                            },
                        model = model,
                        contentDescription = null
                    )

                    Row(
                        modifier = Modifier.align(Alignment.BottomStart)
                            .padding(top = if (model.isNotEmpty()) max(0.dp, bannerHeight - 40.dp) else 0.dp)
                    ) {
                        Spacer(modifier = Modifier.width(16.dp))

                        ProfilePicture(
                            onClick = {},
                            artistMashup = info.mashup,
                            processImageIntent = { intent -> viewModel.processImageIntent(intent) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

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

        LazyVerticalGrid(
            columns = GridCells.Fixed(screenType.shopColumns),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(Padding)
        ) {
            items(
                count = listings.itemCount,
                key = listings.itemKey { it.name + it.compositeUrl }
            ) { index ->
                val nft = listings[index]
                nft?.let {
                    ShopItem(
                        nft = nft,
                        selectId = {
                            parentNavController.navigate(route = MainRoutes.Shop(nft.productInfo?.id))
                        },
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