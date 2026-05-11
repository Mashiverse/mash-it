package com.mashiverse.mashit.ui.screens.shop.special

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.coinbase.android.nativesdk.CoinbaseWalletSDK
import com.mashiverse.mashit.data.models.sys.screens.ScreenInfo
import com.mashiverse.mashit.data.states.shop.ShopIntent
import com.mashiverse.mashit.data.states.sys.DialogIntent
import com.mashiverse.mashit.ui.default.dialogs.Dialog
import com.mashiverse.mashit.ui.default.indicators.LoadingIndicator
import com.mashiverse.mashit.ui.default.modals.ItemPreviewModal
import com.mashiverse.mashit.ui.default.modals.MashiDetailsSection
import com.mashiverse.mashit.ui.screens.shop.regular.ShopItem
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.MediumPadding
import com.mashiverse.mashit.ui.theme.Padding
import com.mashiverse.mashit.ui.theme.SmallPadding
import com.mashiverse.mashit.utils.helpers.sys.detectScreenType
import com.mashiverse.mashit.utils.helpers.sys.getItemWidthAndHeight
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpecialDrop(slug: String) {
    val config = LocalConfiguration.current

    val screenType = config.detectScreenType()
    val (width, height) = config.getItemWidthAndHeight(screenType.shopColumns)

    val viewModel = hiltViewModel<SpecialDropViewModel>()
    val specialDropUiState = viewModel.specialDropUiState

    LaunchedEffect(slug) {
        Timber.tag("GG").d(slug)
        viewModel.getDrop(slug)
    }

    val previewState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

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

    Timber.tag("GG").d(specialDropUiState.dropInfo.toString())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Padding)
    ) {
        specialDropUiState.dropInfo?.let { info ->
            Column {
                Box {
                    val model =
                        if (screenType == ScreenInfo.COMPACT) info.mobileImageUrl else info.desktopImageUrl

                    AsyncImage(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(4)),
                        model = model,
                        contentDescription = null
                    )
                }

                Spacer(modifier = Modifier.height(SmallPadding))

                Text(
                    text = info.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = ContentAccentColor
                )

                Text(
                    text = info.description ?: "",
                    fontSize = 14.sp,
                    color = ContentColor
                )
            }
        }

        Spacer(modifier = Modifier.height(Padding))

        LazyVerticalGrid(
            columns = GridCells.Fixed(screenType.shopColumns),
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(MediumPadding),
            verticalArrangement = Arrangement.spacedBy(Padding)
        ) {
            items(
                specialDropUiState.dropItems.size
            ) { index ->
                val nft = specialDropUiState.dropItems[index]

                ShopItem(
                    nft = nft,
                    processShopIntent = { intent -> viewModel.processShopIntent(intent) },
                    clientRef = clientRef,
                    processImageIntent = { intent -> viewModel.processImageIntent(intent) },
                    processWeb3Intent = { intent -> viewModel.processWeb3Intent(intent) },
                    imageWidth = width,
                    imageHeight = height,
                )
            }
        }

        if (specialDropUiState.isExpanded) {
            specialDropUiState.selectedNft?.let { nft ->
                ItemPreviewModal(
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
    }

    if (specialDropUiState.dropInfo == null) {
        LoadingIndicator(text = "Loading")
    }

    specialDropUiState.dialogContent?.let { content ->
        Dialog(content) {
            viewModel.processDialogIntent(DialogIntent.OnClear)
        }
    }
}