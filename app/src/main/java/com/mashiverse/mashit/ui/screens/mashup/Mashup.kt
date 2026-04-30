package com.mashiverse.mashit.ui.screens.mashup

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.mashiverse.mashit.data.models.mashi.Nft
import com.mashiverse.mashit.data.models.mashi.SortType
import com.mashiverse.mashit.data.models.mashi.TraitType
import com.mashiverse.mashit.data.models.mashup.colors.ColorType
import com.mashiverse.mashit.data.states.mashup.ActionsIntent
import com.mashiverse.mashit.data.states.sys.DialogIntent.OnClear
import com.mashiverse.mashit.ui.default.dialogs.Dialog
import com.mashiverse.mashit.ui.default.indicators.LoadingIndicator
import com.mashiverse.mashit.ui.default.indicators.NotConnected
import com.mashiverse.mashit.ui.default.sorting.Sorting
import com.mashiverse.mashit.ui.screens.mashup.actions.MashupActions
import com.mashiverse.mashit.ui.screens.mashup.categories.CategorySelector
import com.mashiverse.mashit.ui.screens.mashup.categories.sections.CollectiblesCategory
import com.mashiverse.mashit.ui.screens.mashup.categories.sections.TraitsCategoryItems
import com.mashiverse.mashit.ui.screens.mashup.color.ColorSheet
import com.mashiverse.mashit.ui.screens.mashup.preview.MashupPreview
import com.mashiverse.mashit.ui.theme.Padding
import com.mashiverse.mashit.ui.theme.SmallPadding
import com.mashiverse.mashit.ui.theme.XLHolderHeight
import com.mashiverse.mashit.ui.theme.XLHolderWidth
import com.mashiverse.mashit.utils.color.helpers.toHexColor
import com.mashiverse.mashit.utils.helpers.nft.getTraitsByType
import kotlinx.coroutines.launch

@SuppressLint("ConfigurationScreenWidthHeight", "FlowOperatorInvokedInComposition",
    "CoroutineCreationDuringComposition"
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Mashup(searchQuery: State<String>) {
    val searchQuery by remember(searchQuery.value) {
        mutableStateOf(searchQuery.value)
    }

    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val viewModel = hiltViewModel<MashupViewModel>()

    // Modals
    val colorChangingState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val previewState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val traitsGridState = rememberLazyGridState()
    val collectiblesVState = rememberLazyListState()

    var height by remember { mutableStateOf(0.dp) }

    val mashupUiState by remember { viewModel.mashupUiState }

    val selectedColorType by remember(mashupUiState.selectedColorType) {
        mutableStateOf(
            mashupUiState.selectedColorType
        )
    }

    val currentColor = remember(selectedColorType, mashupUiState.colors) {
        when (selectedColorType) {
            ColorType.BASE -> mashupUiState.colors.base
            ColorType.EYES -> mashupUiState.colors.eyes
            ColorType.HAIR -> mashupUiState.colors.hair
        }
    }

    val previousColor = remember(mashupUiState.mashupDetails, selectedColorType) {
        when (selectedColorType) {
            ColorType.BASE -> mashupUiState.mashupDetails.colors.base
            ColorType.EYES -> mashupUiState.mashupDetails.colors.eyes
            ColorType.HAIR -> mashupUiState.mashupDetails.colors.hair
        }
    }

    var nfts by remember { mutableStateOf<List<Nft>>(emptyList()) }

    LaunchedEffect(mashupUiState.nfts, searchQuery) {
        val temp = mutableListOf<Nft>()
        mashupUiState.nfts.forEach { nft ->
            temp.add(nft)
        }

        nfts = if (searchQuery.isEmpty()) {
            temp
        } else {
            temp.filter {
                it.name.lowercase().contains(searchQuery.lowercase())
                        || it.author.lowercase().contains(searchQuery.lowercase())
            }
        }
    }

    val selectedTraitUrl by remember(mashupUiState.mashupDetails, mashupUiState.selectedCategory) {
        derivedStateOf {
            mashupUiState.mashupDetails.assets.first { it.type == mashupUiState.selectedCategory }.url
                ?: ""
        }
    }

    var sortType by remember { mutableStateOf(SortType.NEWEST) }

    val sortedNfts = remember(sortType, nfts) {
        when (sortType) {
            SortType.NEWEST -> nfts.sortedByDescending { it.owned?.get(0)?.timestamp }
            SortType.OLDEST -> nfts.sortedBy { it.owned?.get(0)?.timestamp }
            // Sort by Name (A-Z), then by Mint (Ascending)
            SortType.ALPHABET_ASC -> nfts
                .sortedWith(compareBy<Nft> { it.name.lowercase() }
                    .thenBy { it.owned?.firstOrNull()?.mint })

            // Sort by Name (Z-A), then by Mint (Ascending)
            SortType.ALPHABET_DESC -> nfts
                .sortedWith(compareByDescending<Nft> { it.name.lowercase() }
                    .thenBy { it.owned?.firstOrNull()?.mint })
        }
    }

    val traits by remember(mashupUiState.selectedCategory, sortedNfts) {
        derivedStateOf {
            val traits =
                getTraitsByType(sortedNfts)[mashupUiState.selectedCategory]
                    ?: emptyList()

            if (mashupUiState.selectedCategory != TraitType.BACKGROUND) {
                traits.distinctBy { it.avatarName }
            } else {
                traits
            }
        }
    }

    Column {
        if (mashupUiState.wallet != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = Padding)
            ) {
                MashupActions(
                    mashupDetails = mashupUiState.mashupDetails.copy(colors = mashupUiState.colors),
                    modifier = Modifier
                        .height(XLHolderHeight)
                        .width(XLHolderWidth)
                        .clickable { viewModel.processActionsIntent(ActionsIntent.OnPreview) },
                    holderWidth = XLHolderWidth,
                    processImageIntent = { intent -> viewModel.processImageIntent(intent) },
                    processActionsIntent = { intent -> viewModel.processActionsIntent(intent) }
                )

                Column(
                    modifier = Modifier
                        .wrapContentHeight()
                        .onSizeChanged { size ->
                            height = with(density) { size.height.toDp() } - SmallPadding
                        },
                ) {
                    Spacer(Modifier.height(Padding))

                    Row{
                        Sorting {
                            type -> sortType = type
                            scope.launch {
                                collectiblesVState.animateScrollToItem(0)
                                traitsGridState.animateScrollToItem(0)
                            }
                        }

                        CategorySelector(
                            mashupUiState = mashupUiState,
                            processMashupIntent = { intent -> viewModel.processMashupIntent(intent) },
                            gridState = traitsGridState,
                            scope = scope
                        )
                    }

                    if (mashupUiState.isCollectionReady) {
                        if (mashupUiState.isCollectibles) {
                            CollectiblesCategory(
                                nfts = sortedNfts,
                                mashupDetails = mashupUiState.mashupDetails,
                                state = collectiblesVState,
                                scope = scope,
                                processMashupIntent = { intent ->
                                    viewModel.processMashupIntent(
                                        intent
                                    )
                                },
                                processImageIntent = { intent -> viewModel.processImageIntent(intent) }
                            )

                            scope.launch { collectiblesVState.animateScrollToItem(0) }
                        } else {
                            TraitsCategoryItems(
                                traits = traits,
                                selectedTraitUrl = selectedTraitUrl,
                                lazyGridState = traitsGridState,
                                processMashupIntent = { intent ->
                                    viewModel.processMashupIntent(
                                        intent
                                    )
                                },
                                processImageIntent = { intent -> viewModel.processImageIntent(intent) }
                            )
                        }
                    } else {
                        LoadingIndicator(text = "Loading")
                    }
                }
            }
        } else {
            NotConnected()
        }

        if (mashupUiState.isColorChange) {
            ColorSheet(
                sheetState = colorChangingState,
                initialColor = previousColor.toHexColor(),
                color = currentColor.toHexColor(),
                scope = scope,
                selectedColorType = selectedColorType,
                processMashupIntent = { intent -> viewModel.processMashupIntent(intent) },
                processActionsIntent = { intent -> viewModel.processActionsIntent(intent) },
                height = height
            )
        }

        if (mashupUiState.isPreview) {
            MashupPreview(
                closeBottomSheet = { viewModel.processActionsIntent(ActionsIntent.OnPreviewDismiss) },
                sheetState = previewState,
                scope = scope,
                mashupDetails = mashupUiState.mashupDetails.copy(colors = mashupUiState.colors),
                processImageIntent = { intent -> viewModel.processImageIntent(intent) },
                height = height
            )
        }
    }

    if (mashupUiState.isDownloading) {
        LoadingIndicator("Downloading")
    }

    mashupUiState.dialogContent?.let { content ->
        Dialog(content) {
            viewModel.processDialogIntent(OnClear)
        }
    }
}