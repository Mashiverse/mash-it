package com.mashiverse.mashit.ui.screens.artists.page

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import com.coinbase.android.nativesdk.CoinbaseWalletSDK
import com.mashiverse.mashit.data.intents.DialogIntent
import com.mashiverse.mashit.data.intents.ShopIntent
import com.mashiverse.mashit.data.models.ScreenInfo
import com.mashiverse.mashit.ui.dialogs.Dialog
import com.mashiverse.mashit.ui.nft.MashiBottomSheet
import com.mashiverse.mashit.ui.nft.MashiDetailsSection
import com.mashiverse.mashit.ui.screens.artists.ProfilePicture
import com.mashiverse.mashit.ui.screens.shop.items.SectionLoading
import com.mashiverse.mashit.ui.screens.shop.items.SectionRefresh
import com.mashiverse.mashit.ui.screens.shop.items.ShopItem
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.Padding
import com.mashiverse.mashit.ui.theme.TraitShape
import com.mashiverse.mashit.utils.helpers.sys.detectScreenType
import com.mashiverse.mashit.utils.helpers.sys.getItemWidthAndHeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistPage(alias: String) {
    val config = LocalConfiguration.current
    val density = LocalDensity.current

    val screenType = config.detectScreenType()
    val (width, height) = config.getItemWidthAndHeight(screenType.shopColumns)

    val viewModel = hiltViewModel<ArtistPageViewModel>()
    val artistPageUiState by remember { viewModel.artistPageUiState }

    val listings = artistPageUiState.itemsData.collectAsLazyPagingItems()

    LaunchedEffect(alias) {
        viewModel.onInit(alias)
    }

    val previewState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()



    val appendState = listings.loadState.append

    var clientRef by remember { mutableStateOf<CoinbaseWalletSDK?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val uri = result.data?.data ?: return@rememberLauncherForActivityResult
        clientRef?.handleResponse(uri)
    }

    LaunchedEffect(Unit) {
        clientRef = viewModel.getCoinbaseSdk { intent ->
            launcher.launch(intent)
        }
    }

    var bannerHeight by remember { mutableStateOf(0.dp) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        artistPageUiState.pageInfo?.let { info ->
            Column {
                Box {
                    val model =
                        if (screenType == ScreenInfo.COMPACT) info.bannerUrl else info.desktopBannerUrl

                    AsyncImage(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .fillMaxWidth()
                            .onSizeChanged { size ->
                                with(density) {
                                    bannerHeight = size.height.toDp()
                                }
                            }
                            .clip(RoundedCornerShape(4)),
                        model = model,
                        contentDescription = null
                    )

                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(
                                top = if (model.isNotEmpty()) max(
                                    0.dp,
                                    bannerHeight - 40.dp
                                ) else 0.dp
                            )
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
                        processWeb3Intent = { intent -> viewModel.processWeb3Intent(intent) },
                        processImageIntent = { intent -> viewModel.processImageIntent(intent) },
                        processShopIntent = { intent -> viewModel.processShopIntent(intent) },
                        clientRef = clientRef!!,
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

    if (artistPageUiState.isExpanded) {
        artistPageUiState.selectedNft?.let { nft ->
            MashiBottomSheet(
                selectedNft = nft,
                sheetState = previewState,
                closeBottomSheet = { viewModel.processShopIntent(ShopIntent.OnNftDeselect) },
                processImageIntent = { intent -> viewModel.processImageIntent(intent) }
            ) {
                MashiDetailsSection(
                    nft = nft,
                    scope = scope,
                    closeBottomSheet = {
                        viewModel.processShopIntent(ShopIntent.OnNftDeselect)
                    },
                    sheetState = previewState,
                    clientRef = clientRef,
                    processWeb3Intent = { intent -> viewModel.processWeb3Intent(intent) }
                )
            }
        }
    }

    artistPageUiState.dialogContent?.let { content ->
        Dialog(content) {
            viewModel.processDialogIntent(DialogIntent.OnClear)
        }
    }
}