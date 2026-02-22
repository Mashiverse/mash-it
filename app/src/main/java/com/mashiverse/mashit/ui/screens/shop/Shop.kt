package com.mashiverse.mashit.ui.screens.shop

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.coinbase.android.nativesdk.CoinbaseWalletSDK
import com.mashiverse.mashit.data.models.dialog.DialogContent
import com.mashiverse.mashit.data.models.image.ImageType
import com.mashiverse.mashit.data.models.mashi.Nft
import com.mashiverse.mashit.data.models.wallet.WalletPreferences
import com.mashiverse.mashit.ui.screens.components.dialogs.Dialog
import com.mashiverse.mashit.ui.screens.components.header.CategoryHeader
import com.mashiverse.mashit.ui.screens.components.nft.MashiBottomSheet
import com.mashiverse.mashit.ui.screens.components.nft.MashiDetailsSection
import com.mashiverse.mashit.ui.screens.shop.sections.ShopCategory
import com.mashiverse.mashit.ui.screens.shop.sections.ShopSection
import com.mashiverse.mashit.ui.theme.PaddingSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Shop(listingId: String?) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var isBottomSheet by remember { mutableStateOf(false) }

    val viewModel = hiltViewModel<ShopViewModel>()
    val pagingItems = viewModel.shopPagingData.collectAsLazyPagingItems()

    val selectedListing by remember { viewModel.selectedNft }
    val walletPreferences = viewModel.walletPreferences.collectAsState(WalletPreferences(null))

    val selectId = { id: String ->
        viewModel.selectId(id)
        isBottomSheet = true
    }

    var category by remember { mutableStateOf<String?>(null) }
    var categoryItems by remember { mutableStateOf<LazyPagingItems<Nft>?>(null) }

    val onCategorySelect = { categoryName: String, items: LazyPagingItems<Nft> ->
        category = categoryName
        categoryItems = items
    }

    val onCategoryClose = {
        category = null
    }

    LaunchedEffect(listingId) {
        if (!listingId.isNullOrEmpty()) {
            selectId.invoke(listingId)
            isBottomSheet = true
        }
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

    val onMint = { listingId: String, price: Double ->
        if (clientRef != null && walletPreferences.value.wallet != null) {
            viewModel.mint(
                client = clientRef!!,
                fromAddress = walletPreferences.value.wallet!!,
                listingId = listingId,
                price = price
            )
        } else {
            viewModel.setDialogContent(
                DialogContent(
                    title = "Not Authenticated",
                    text = "Please connect your wallet to continue"
                )
            )
        }
    }

    val getSoldQty = { listingId: Int, callback: (Int) -> Unit ->
        viewModel.getTotalSold(listingId, callback)
    }

    val dialogContent by remember {
        viewModel.dialogContent
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = PaddingSize)
    ) {
        CategoryHeader(title = "Shop")

        if (category == null) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(PaddingSize)
            ) {
                item {
                    ShopSection(
                        sectionName = "Recently Released",
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
                        getSoldQty = getSoldQty,
                        onMint = onMint,
                        onCategorySelect = onCategorySelect
                    )
                }
            }
        } else {
            ShopCategory(
                categoryName = category!!,
                categoryItems = categoryItems!!,
                selectId = selectId,
                getImageType = { url ->
                    var imageType: ImageType? = null
                    viewModel.getTraitTypeEntity(url) { type -> imageType = type }
                    imageType
                },
                setImageType = { type, data ->
                    viewModel.insertTraitType(url = data, imageType = type)
                },
                getSoldQty = getSoldQty,
                onMint = onMint,
                onCategoryClose = onCategoryClose
            )
        }
    }

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
                    getSoldQty = getSoldQty,
                    onMint = onMint
                )
            }
        }
    }

    if (dialogContent != null) {
        Dialog(dialogContent!!) {
            viewModel.clearDialog()
        }
    }
}