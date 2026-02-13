package com.mashiverse.mashit.ui.screens.shop

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.coinbase.android.nativesdk.CoinbaseWalletSDK
import com.mashiverse.mashit.data.models.image.ImageType
import com.mashiverse.mashit.ui.screens.components.header.CategoryHeader
import com.mashiverse.mashit.ui.screens.components.nft.MashiBottomSheet
import com.mashiverse.mashit.ui.screens.components.nft.MashiDetailsSection
import com.mashiverse.mashit.ui.theme.PaddingSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Shop() {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var isBottomSheet by remember { mutableStateOf(false) }

    val viewModel = hiltViewModel<ShopViewModel>()
    val pagingItems = viewModel.shopPagingData.collectAsLazyPagingItems()

    val selectedListing by remember { viewModel.selectedNft }

    val selectId = { id: String ->
        viewModel.selectId(id)
        isBottomSheet = true
    }

    val closeBottomShit = { isBottomSheet = false }

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

    val getSoldQty: suspend (Int) -> Int = { listingId ->
        viewModel.getTotalSold(listingId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = PaddingSize)
    ) {
        CategoryHeader(title = "Shop")

//        LazyColumn(
//            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.spacedBy(PaddingSize)
//        ) {
//            item {
        ShopSection(
            sectionName = "Ervindas",
            selectId = selectId,
            sectionItems = pagingItems,
            getImageType = { url ->
                var imageType: ImageType? = null
                viewModel.getTraitTypeEntity(url) { type -> imageType = type }
                imageType
            },
            setImageType = { type, data ->
                viewModel.insertTraitType(url = data, imageType = type)
            },
            getSoldQty = getSoldQty
        )
    }
//        }
//    }

    if (isBottomSheet) {
        selectedListing?.let { nft ->
            MashiBottomSheet(
                selectedNft = nft,
                sheetState = sheetState,
                closeBottomShit = closeBottomShit,
                getImageType = { url ->
                    var imageType: ImageType? = null
                    viewModel.getTraitTypeEntity(url) { type -> imageType = type }
                    imageType
                },
                setImageType = { type, data ->
                    viewModel.insertTraitType(url = data, imageType = type)
                },
            ) {
                MashiDetailsSection(
                    nft = nft,
                    scope = scope,
                    closeBottomShit = closeBottomShit,
                    sheetState = sheetState,
                    getSoldQty = getSoldQty
                )
            }
        }
    }
}