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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.coinbase.android.nativesdk.CoinbaseWalletSDK
import com.mashiverse.mashit.data.intents.DialogIntent
import com.mashiverse.mashit.data.intents.ShopIntent
import com.mashiverse.mashit.data.models.ShopDataType
import com.mashiverse.mashit.ui.dialogs.Dialog
import com.mashiverse.mashit.ui.nft.MashiBottomSheet
import com.mashiverse.mashit.ui.nft.MashiDetailsSection
import com.mashiverse.mashit.ui.screens.shop.sections.Category
import com.mashiverse.mashit.ui.screens.shop.sections.ShopSection
import com.mashiverse.mashit.ui.theme.Padding

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

    val previewState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val viewModel = hiltViewModel<ShopViewModel>()
    val shopUiState by remember { viewModel.shopUiState }

    LaunchedEffect(listingId) {
        if (!listingId.isNullOrEmpty()) {
            viewModel.processShopIntent(ShopIntent.OnNftSelect(listingId))
        }
    }


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

    LaunchedEffect(searchQuery) {
        val dataType = if (searchQuery.isNotEmpty()) {
            ShopDataType.SEARCH
        } else {
            ShopDataType.RECENT
        }

        viewModel.processShopIntent(ShopIntent.OnDataTypeSelect(dataType, searchQuery))
        if (searchQuery.isNotEmpty()) {
            viewModel.processShopIntent(ShopIntent.OnCategorySelect)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        if (shopUiState.isCategory) {
            Category(
                shopUiState = shopUiState,
                clientRef = clientRef!!,
                onSearchQueryClear = if (searchQuery.isNotEmpty()) {
                    {
                        clearSearchQuery.invoke()
                        viewModel.processShopIntent(ShopIntent.OnCategoryClose)
                    }
                } else null,
                processWeb3Intent = { intent -> viewModel.processWeb3Intent(intent) },
                processImageIntent = { intent -> viewModel.processImageIntent(intent) },
                processShopIntent = { intent -> viewModel.processShopIntent(intent) }
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(Padding)
            ) {
                item {
                    clientRef?.let {
                        ShopSection(
                            shopUiState = shopUiState,
                            processWeb3Intent = { intent -> viewModel.processWeb3Intent(intent) },
                            processImageIntent = { intent -> viewModel.processImageIntent(intent) },
                            processShopIntent = { intent -> viewModel.processShopIntent(intent) },
                            clientRef = clientRef!!,
                        )
                    }
                }
            }
        }
    }

    if (shopUiState.isExpanded) {
        shopUiState.selectedNft?.let { nft ->
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

    shopUiState.dialogContent?.let { content ->
        Dialog(content) {
            viewModel.processDialogIntent(DialogIntent.OnClear)
        }
    }
}