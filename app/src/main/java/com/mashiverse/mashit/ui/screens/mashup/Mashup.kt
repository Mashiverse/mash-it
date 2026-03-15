package com.mashiverse.mashit.ui.screens.mashup

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
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
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.mashiverse.mashit.data.intents.ActionsIntent
import com.mashiverse.mashit.data.intents.DialogIntent.Clear
import com.mashiverse.mashit.data.intents.MashupIntent
import com.mashiverse.mashit.data.models.mashup.colors.ColorType
import com.mashiverse.mashit.data.models.nft.Nft
import com.mashiverse.mashit.data.models.nft.Owned
import com.mashiverse.mashit.data.models.nft.TraitType
import com.mashiverse.mashit.data.models.nft.mappers.fromEntities
import com.mashiverse.mashit.ui.screens.components.dialogs.Dialog
import com.mashiverse.mashit.ui.screens.components.header.CategoryHeader
import com.mashiverse.mashit.ui.screens.components.placeholder.NotConnected
import com.mashiverse.mashit.ui.screens.mashup.actions.MashupActions
import com.mashiverse.mashit.ui.screens.mashup.categories.MashupCategories
import com.mashiverse.mashit.ui.screens.mashup.categories.MashupCategoryItems
import com.mashiverse.mashit.ui.screens.mashup.color.ColorSheet
import com.mashiverse.mashit.ui.screens.mashup.preview.MashupSheet
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.ExtraSmallPaddingSize
import com.mashiverse.mashit.ui.theme.MashiHolderShape
import com.mashiverse.mashit.ui.theme.MaxMashiHolderHeight
import com.mashiverse.mashit.ui.theme.MaxMashiHolderWidth
import com.mashiverse.mashit.ui.theme.PaddingSize
import com.mashiverse.mashit.ui.theme.SmallPaddingSize
import com.mashiverse.mashit.utils.color.helpers.toHexColor
import com.mashiverse.mashit.utils.helpers.TraitsHelper
import kotlinx.coroutines.flow.map
import timber.log.Timber

@SuppressLint("ConfigurationScreenWidthHeight", "FlowOperatorInvokedInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Mashup(searchQuery: State<String>) {
    val searchQuery by remember(searchQuery.value) {
        mutableStateOf(searchQuery.value)
    }

    LocalContext.current
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val config = LocalConfiguration.current

    val compositeWidth = config.screenWidthDp.dp * 0.56f
    val compositeHeight = (compositeWidth / 3) * 4

    // Bottom Sheet States
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isBottomSheet by remember { mutableStateOf(false) }
    val previewSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val lazyGridState = rememberLazyGridState()
    val viewModel = hiltViewModel<MashupViewModel>()
    var height by remember { mutableStateOf(0.dp) }


    val mashupUiState by remember { viewModel.mashupUiState }

    val selectedColorType by remember(mashupUiState.selectedColorType) {
        mutableStateOf(mashupUiState.selectedColorType)
    }

    var colorBuffer by remember(mashupUiState.mashupDetails) {
        mutableStateOf(mashupUiState.mashupDetails.colors)
    }

    val selectedCategory by remember(mashupUiState.selectedCategory) {
        mutableStateOf(mashupUiState.selectedCategory)
    }

    val currentColorHex by remember(selectedColorType, colorBuffer) {
        derivedStateOf {
            when (selectedColorType) {
                ColorType.BASE -> colorBuffer.base
                ColorType.EYES -> colorBuffer.eyes
                ColorType.HAIR -> colorBuffer.hair
            }
        }
    }

    var nfts by remember { mutableStateOf<List<Nft>>(emptyList()) }

    LaunchedEffect(mashupUiState.nfts, searchQuery) {
        val temp = mutableListOf<Nft>()
        mashupUiState.nfts.forEach { nft ->
            nft.owned?.forEach { owned ->
                temp.add(
                    nft.copy(
                        owned = listOf(
                            Owned(mint = owned.mint, timestamp = owned.timestamp)
                        )
                    )
                )
            }
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


    val traits by remember(
        mashupUiState.selectedCategory,
        nfts
    ) {
        derivedStateOf {
            val traits =
                TraitsHelper.getTraitsByType(mashupUiState.nfts)[mashupUiState.selectedCategory]
                    ?: emptyList()

            if (mashupUiState.selectedCategory != TraitType.BACKGROUND) {
                traits.distinctBy { it.avatarName }
            } else {
                traits
            }
        }
    }

    Column {
        CategoryHeader(title = "Mashup")
        Spacer(modifier = Modifier.height(ExtraSmallPaddingSize))

        if (mashupUiState.wallet != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = PaddingSize)
            ) {
                MashupActions(
                    mashupDetails = mashupUiState.mashupDetails.copy(colors = colorBuffer),
                    modifier = Modifier
                        .height(min(compositeHeight, MaxMashiHolderHeight))
                        .width(min(compositeWidth, MaxMashiHolderWidth))
                        .clickable { viewModel.processActionsIntent(ActionsIntent.OnPreviewDismiss) }
                        .border(width = 0.4.dp, shape = MashiHolderShape, color = ContentColor),
                    holderWidth = compositeWidth,
                    processImageIntent = { intent -> viewModel.processImageIntent(intent) },
                    processMashupIntent = { intent -> viewModel.processActionsIntent(intent) }
                )

                Column(
                    modifier = Modifier
                        .wrapContentHeight()
                        .onSizeChanged { size ->
                            height = with(density) { size.height.toDp() } - SmallPaddingSize
                        },
                ) {
                    Spacer(Modifier.height(SmallPaddingSize))

                    MashupCategories(
                        onCategorySelect = {
                            viewModel.processMashupIntent(
                                MashupIntent.OnCategorySelect(
                                    scope = scope,
                                    state = lazyGridState,
                                    selectedCategory = selectedCategory
                                )
                            )
                        },
                        selectedCategory = selectedCategory
                    )

                    Spacer(Modifier.height(SmallPaddingSize))

                    MashupCategoryItems(
                        modifier = Modifier.fillMaxHeight(),
                        lazyGridState = lazyGridState,
                        traits = traits,
                        processMashupIntent = { intent -> viewModel.processMashupIntent(intent) },
                        processImageIntent = { intent -> viewModel.processImageIntent(intent) }
                    )
                }
            }
        } else {
            NotConnected()
        }

        // COLOR SHEET
        if (isBottomSheet) {
            ColorSheet(
                sheetState = sheetState,
                color = currentColorHex.toHexColor(),
                scope = scope,
                selectedColorType = selectedColorType,
                processMashupIntent = { intent -> viewModel.processMashupIntent(intent) },
                processActionsIntent = { intent -> viewModel.processActionsIntent(intent) },
                height = height
            )
        }

        if (mashupUiState.isPreview) {
            MashupSheet(
                closeBottomShit = { viewModel.processActionsIntent(ActionsIntent.OnPreviewDismiss) },
                sheetState = previewSheetState,
                scope = scope,
                mashupDetails = mashupUiState.mashupDetails.copy(colors = colorBuffer),
                processImageIntent = { intent -> viewModel.processImageIntent(intent) },
                height = height
            )
        }
    }

    mashupUiState.dialogContent?.let { content ->
        Dialog(content) {
            viewModel.processDialogIntent(Clear)
        }
    }
}