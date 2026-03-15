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
import androidx.compose.runtime.State
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
import com.mashiverse.mashit.data.models.nft.Nft
import com.mashiverse.mashit.data.models.wallet.WalletPreferences
import com.mashiverse.mashit.ui.screens.components.dialogs.Dialog
import com.mashiverse.mashit.ui.screens.components.header.CategoryHeader
import com.mashiverse.mashit.ui.screens.components.nft.MashiBottomSheet
import com.mashiverse.mashit.ui.screens.components.nft.MashiDetailsSection
import com.mashiverse.mashit.ui.screens.shop.sections.SearchCategory
import com.mashiverse.mashit.ui.screens.shop.sections.ShopCategory
import com.mashiverse.mashit.ui.screens.shop.sections.ShopSection
import com.mashiverse.mashit.ui.theme.PaddingSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Shop(
    searchQuery: State<String>,
    clearSearchQuery: () -> Unit,
    listingId: String?
) {
    var searchQuery by remember(searchQuery.value) {
        mutableStateOf(searchQuery.value)
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var isBottomSheet by remember { mutableStateOf(false) }

    val viewModel = hiltViewModel<ShopViewModel>()
    val pagingItems = viewModel.shopPagingData.collectAsLazyPagingItems()

    val allListings by remember { viewModel.allListings }
    var searchedListings by remember { mutableStateOf<List<Nft>>(emptyList()) }

    LaunchedEffect(searchQuery, allListings) {
        searchedListings = if (searchQuery.length >= 3) {
            allListings.filter {
                it.name.lowercase().contains(searchQuery.lowercase()) || it.author.lowercase()
                    .contains(searchQuery.lowercase())
            }
        } else {
            emptyList()
        }
    }

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

    val onMint = { listingId: String, price: Double, isPolCurrency: Boolean ->
        if (isPolCurrency) {
            viewModel.setDialogContent(
                DialogContent(
                    title = "POL Currency",
                    text = "We currently don't support POL minting"
                )
            )
        } else if (clientRef != null && walletPreferences.value.wallet != null) {
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

    val onSearchClear = {
        clearSearchQuery.invoke()
        searchedListings = emptyList()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = PaddingSize)
    ) {
        CategoryHeader(title = "Shop")

        if (searchedListings.isNotEmpty()) {
            SearchCategory(
                items = searchedListings,
                selectId = selectId,
                processImageIntent = { intent -> viewModel.processImageIntent(intent) },
                getSoldQty = getSoldQty,
                onMint = onMint,
                onSearchClear = onSearchClear
            )
        } else if (category == null) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(PaddingSize)
            ) {
                item {
                    ShopSection(
                        sectionName = "Recently Released",
                        selectId = selectId,
                        sectionItems = pagingItems,
                        processImageIntent = { intent -> viewModel.processImageIntent(intent) },
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
                processImageIntent = { intent -> viewModel.processImageIntent(intent) },
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
                processImageIntent = { intent -> viewModel.processImageIntent(intent) }
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