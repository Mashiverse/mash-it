package com.mashiverse.mashit.ui.screens.collection

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.mashiverse.mashit.data.models.image.ImageType
import com.mashiverse.mashit.data.models.nft.Nft
import com.mashiverse.mashit.data.models.nft.Owned
import com.mashiverse.mashit.data.models.nft.mappers.fromEntities
import com.mashiverse.mashit.data.models.wallet.WalletPreferences
import com.mashiverse.mashit.ui.screens.components.header.CategoryHeader
import com.mashiverse.mashit.ui.screens.components.nft.MashiBottomSheet
import com.mashiverse.mashit.ui.screens.components.nft.MashiDetailsSection
import com.mashiverse.mashit.ui.screens.components.nft.trait.MintedTrait
import com.mashiverse.mashit.ui.screens.components.placeholder.NotConnected
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.MashiHolderShape
import com.mashiverse.mashit.ui.theme.PaddingSize
import com.mashiverse.mashit.ui.theme.SmallPaddingSize
import kotlinx.coroutines.flow.map


@SuppressLint("ConfigurationScreenWidthHeight", "FlowOperatorInvokedInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Collection(searchQuery: State<String>) {
    val searchQuery by remember(searchQuery.value) {
        mutableStateOf(searchQuery.value)
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var isBottomSheet by remember { mutableStateOf(false) }

    val viewModel = hiltViewModel<CollectionViewModel>()
    val collection by viewModel.collectionFlow
        .map { it.fromEntities() }
        .collectAsState(emptyList())

    var ownedNfts by remember { mutableStateOf<List<Nft>>(emptyList()) }

    LaunchedEffect(collection, searchQuery) {
        val nfts = mutableListOf<Nft>()
        collection.forEach { nft ->
            nft.owned?.forEach { owned ->
                nfts.add(
                    nft.copy(
                        owned = listOf(
                            Owned(mint = owned.mint, timestamp = owned.timestamp)
                        )
                    )
                )
            }
        }
        ownedNfts = if (searchQuery.isEmpty()) {
            nfts
        } else {
            nfts.filter {
                it.name.lowercase().contains(searchQuery.lowercase())
                        || it.author.lowercase().contains(searchQuery.lowercase())
            }
        }
    }

    val walletPreferences = viewModel.walletPreferences.collectAsState(WalletPreferences(null))

    val selectedMashi: Nft? by remember {
        derivedStateOf {
            viewModel.selectedNft.value
        }
    }
    val selectMashi = { mashi: Nft ->
        viewModel.selectMashi(mashi)
        isBottomSheet = true
    }

    val closeBottomShit = {
        isBottomSheet = false
    }

    val config = LocalConfiguration.current
    val mashiHolderWidth = (config.screenWidthDp.dp - 2 * PaddingSize - 2 * SmallPaddingSize) / 3
    val mashiHolderHeight = mashiHolderWidth * 4 / 3

    if (walletPreferences.value.wallet != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = PaddingSize),
        ) {
            CategoryHeader("Collection")

            Spacer(modifier = Modifier.height(SmallPaddingSize))

            LazyVerticalGrid(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(SmallPaddingSize),
                horizontalArrangement = Arrangement.spacedBy(SmallPaddingSize),
                columns = GridCells.Fixed(3)
            ) {
                items(ownedNfts.size) { i ->
                    MintedTrait(
                        modifier = Modifier
                            .height(mashiHolderHeight)
                            .width(mashiHolderWidth)
                            .border(width = 0.2.dp, shape = MashiHolderShape, color = ContentColor),
                        onClick = { selectMashi.invoke(ownedNfts[i]) },
                        data = ownedNfts[i].compositeUrl,
                        processImageIntent = { intent -> viewModel.processImageIntent(intent) },
                        mint = ownedNfts[i].owned!![0].mint
                    )
                }
            }
        }

        if (isBottomSheet) {
            selectedMashi?.let {
                MashiBottomSheet(
                    selectedNft = selectedMashi!!,
                    closeBottomShit = closeBottomShit,
                    processImageIntent = { intent -> viewModel.processImageIntent(intent) },
                    sheetState = sheetState,
                ) {
                    MashiDetailsSection(
                        nft = selectedMashi!!,
                        scope = scope,
                        closeBottomShit = closeBottomShit,
                        sheetState = sheetState,
                    )
                }
            }
        }
    } else {
        NotConnected()
    }
}