package com.mashiverse.mashit.ui.screens.shop

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
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
import com.coinbase.android.nativesdk.CoinbaseWalletSDK
import com.mashiverse.mashit.data.models.image.ImageType
import com.mashiverse.mashit.data.models.wallet.WalletPreferences
import com.mashiverse.mashit.ui.screens.header.CategoryHeader
import com.mashiverse.mashit.ui.screens.mashi.MashiBottomSheet
import com.mashiverse.mashit.ui.theme.PaddingSize
import kotlinx.coroutines.launch
import java.math.BigInteger

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Shop() {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var isBottomSheet by remember { mutableStateOf(false) }

    val viewModel = hiltViewModel<ShopViewModel>()
    val listings by remember {
        viewModel.listings
    }
    val selectedMashi by remember {
        viewModel.selectedMashi
    }
    val selectId = { id: String ->
        viewModel.selectId(id)
        isBottomSheet = true
    }

    val closeBottomShit = {
        isBottomSheet = false
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



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = PaddingSize)
    ) {
        CategoryHeader(title = "Shop")

        Button(onClick = {
            scope.launch {
                viewModel.preAuthorizeUsdc(clientRef!!)
            }

        }) {
            Text("Mint")
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(PaddingSize)
        ) {
            item {
                ShopSection(
                    sectionName = "Ervindas",
                    selectId = selectId,
                    sectionItems = listings,
                    getImageType = { url: String ->
                        var imageType: ImageType? = null
                        viewModel.getTraitTypeEntity(url) { type: ImageType? ->
                            imageType = type
                        }
                        imageType
                    },
                    setImageType = { imageType: ImageType, data: String ->
                        viewModel.insertTraitType(
                            url = data,
                            imageType = imageType
                        )
                    }
                )
            }
        }
    }

    if (isBottomSheet) {
        selectedMashi?.let {
            MashiBottomSheet(
                selectedMashi = selectedMashi!!,
                sheetState = sheetState,
                closeBottomShit = closeBottomShit,
                scope = scope,
                getImageType = { url: String ->
                    var imageType: ImageType? = null
                    viewModel.getTraitTypeEntity(url) { type: ImageType? ->
                        imageType = type
                    }
                    imageType
                },
                setImageType = { imageType: ImageType, data: String ->
                    viewModel.insertTraitType(
                        url = data,
                        imageType = imageType
                    )
                }
            )
        }
    }
}