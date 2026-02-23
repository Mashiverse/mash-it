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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.mashiverse.mashit.data.models.image.ImageType
import com.mashiverse.mashit.data.models.mashup.MashupTrait
import com.mashiverse.mashit.data.models.mashup.colors.ColorType
import com.mashiverse.mashit.data.models.nft.Nft
import com.mashiverse.mashit.data.models.nft.Owned
import com.mashiverse.mashit.data.models.nft.Trait
import com.mashiverse.mashit.data.models.nft.TraitType
import com.mashiverse.mashit.data.models.nft.mappers.fromEntities
import com.mashiverse.mashit.data.models.wallet.WalletPreferences
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
import com.mashiverse.mashit.utils.color.helpers.toHexString
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

@SuppressLint("ConfigurationScreenWidthHeight", "FlowOperatorInvokedInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Mashup(searchQuery: State<String>) {
    val searchQuery by remember(searchQuery.value) {
        mutableStateOf(searchQuery.value)
    }

    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val config = LocalConfiguration.current

    val compositeWidth = config.screenWidthDp.dp * 0.56f
    val compositeHeight = (compositeWidth / 3) * 4

    // Bottom Sheet States
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isBottomSheet by remember { mutableStateOf(false) }
    val previewSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isPreviewBottomSheet by remember { mutableStateOf(false) }

    val lazyGridState = rememberLazyGridState()
    val viewModel = hiltViewModel<MashupViewModel>()

    // 1. Source of Truth from ViewModel
    val walletPreferences = viewModel.walletPreferences.collectAsState(WalletPreferences(null))
    val vmDetails by viewModel.mashupDetails
    val selectedColorType by viewModel.selectedColorType

    // 2. The Buffer: This holds unsaved color changes for the UI
    // It resets only if the ViewModel's saved colors change (e.g., after a save or R)
    var colorBuffer by remember(vmDetails) {
        mutableStateOf(vmDetails.colors)
    }

    var height by remember { mutableStateOf(0.dp) }

    val onPngButtonClick = {
        val wallet = walletPreferences.value.wallet
        if (wallet != null) {
            viewModel.startImageUpload(
                wallet = wallet,
                imgType = 0,
                context = ctx
            )
        }
    }

    val onGifButtonClick = {
        val wallet = walletPreferences.value.wallet
        if (wallet != null) {
            viewModel.startImageUpload(
                wallet = wallet,
                imgType = 1,
                context = ctx
            )
        }
    }

    // 3. Actions
    val saveColors = {
        viewModel.changeColors(colorBuffer)
    }

    val resetColors = {
        colorBuffer = vmDetails.colors
    }

    val changeColor: (Color) -> Unit = { newColor ->
        val hex = "#" + newColor.toHexString()
        colorBuffer = when (selectedColorType) {
            ColorType.BASE -> colorBuffer.copy(base = hex)
            ColorType.EYES -> colorBuffer.copy(eyes = hex)
            ColorType.HAIR -> colorBuffer.copy(hair = hex)
        }
    }

    // 4. Derived Hex for the Color Picker
    val currentColorHex by remember(selectedColorType, colorBuffer) {
        derivedStateOf {
            when (selectedColorType) {
                ColorType.BASE -> colorBuffer.base
                ColorType.EYES -> colorBuffer.eyes
                ColorType.HAIR -> colorBuffer.hair
            }
        }
    }

    val changeMashupTrait = { mashupTrait: MashupTrait ->
        viewModel.updateMashup(mashupTrait)
    }

    // Collection Data
    val collection by viewModel.collectionFlow
        .map { it.fromEntities() }
        .collectAsState(emptyList())

    var nfts by remember { mutableStateOf<List<Nft>>(emptyList()) }

    LaunchedEffect(collection, searchQuery) {
        val temp = mutableListOf<Nft>()
        collection.forEach { nft ->
            nft.owned?.forEach { owned ->
                Timber.tag("GG").d(owned.toString())
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

    val onRedoButtonClick = {
        viewModel.redo()
    }
    val onUndoButtonClick = {
        viewModel.undo()
    }

    val traitsByType = remember(nfts) {
        val allMashupTraits = nfts.flatMap { nft ->
            nft.traits?.map { trait ->
                MashupTrait(
                    trait = trait,
                    avatarName = nft.name,
                    mint = if (trait.type == TraitType.BACKGROUND) {
                        nft.owned?.getOrNull(0)?.mint
                    } else {
                        null
                    }
                )
            } ?: emptyList()
        }

        allMashupTraits.groupBy { it.trait.type }
    }

    var selectedCategory by remember { mutableStateOf(TraitType.BACKGROUND) }
    val traits by remember(selectedCategory, nfts) {
        derivedStateOf {
            val traits = traitsByType[selectedCategory] ?: emptyList()

            if (selectedCategory != TraitType.BACKGROUND) {
                traits.distinctBy { it.avatarName }
            } else {
                traits
            }
        }
    }

    val onMashupCategorySelect = { traitType: TraitType ->
        selectedCategory = traitType
        scope.launch { lazyGridState.scrollToItem(0) }
    }

    val onRandomButtonClick = {
        if (nfts.isNotEmpty()) {
            val randomAssets = TraitType.entries.mapNotNull { type ->
                when (type) {
                    TraitType.BACKGROUND, TraitType.EYES, TraitType.BOTTOM, TraitType.UPPER, TraitType.HEAD -> {
                        traitsByType[type]?.randomOrNull()
                    }

                    else -> {
                        val isIncluded = arrayOf(true, false).random()
                        if (isIncluded) {
                            traitsByType[type]?.randomOrNull()
                        } else {
                            MashupTrait(
                                trait = Trait(type = type, url = null),
                                avatarName = ""
                            )
                        }
                    }
                }
            }
            viewModel.randomizeMashup(randomAssets)
        }
    }

    val onResetButtonClick = {
        viewModel.reset()
    }

    val onSaveButtonClick = {
        val wallet = walletPreferences.value.wallet
        if (wallet != null) {
            viewModel.saveMashup(wallet)
        }
    }

    Column {
        CategoryHeader(title = "Mashup")
        Spacer(modifier = Modifier.height(ExtraSmallPaddingSize))

        if (walletPreferences.value.wallet != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = PaddingSize)
            ) {
                MashupActions(
                    mashupDetails = vmDetails.copy(colors = colorBuffer),
                    modifier = Modifier
                        .height(min(compositeHeight, MaxMashiHolderHeight))
                        .width(min(compositeWidth, MaxMashiHolderWidth))
                        .clickable { isPreviewBottomSheet = true }
                        .border(width = 0.4.dp, shape = MashiHolderShape, color = ContentColor),
                    holderWidth = compositeWidth,
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
                    },
                    onColorButtonClick = { isBottomSheet = true },
                    onRandomButtonClick = onRandomButtonClick,
                    onSaveButtonClick = onSaveButtonClick,
                    onPngButtonClick = onPngButtonClick,
                    onGifButtonClick = onGifButtonClick,
                    onResetButtonClick = onResetButtonClick,
                    onUndoButtonClick = onUndoButtonClick,
                    onRedoButtonClick = onRedoButtonClick,
                    onPreviewButtonClick = { isPreviewBottomSheet = true }
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
                        onMashupCategorySelect = onMashupCategorySelect,
                        selectedCategory = selectedCategory
                    )

                    Spacer(Modifier.height(SmallPaddingSize))

                    MashupCategoryItems(
                        modifier = Modifier.fillMaxHeight(),
                        lazyGridState = lazyGridState,
                        traits = traits,
                        changeMashupTrait = changeMashupTrait,
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
        } else {
            NotConnected()
        }

        // COLOR SHEET
        if (isBottomSheet) {
            ColorSheet(
                closeBottomShit = {
                    isBottomSheet = false
                    resetColors()
                },
                saveColors = {
                    saveColors()
                    isBottomSheet = false
                },
                sheetState = sheetState,
                color = currentColorHex.toHexColor(),
                scope = scope,
                changeColor = changeColor,
                selectedColorType = selectedColorType,
                selectColorType = { viewModel.selectColorType(it) },
                height = height
            )
        }

        if (isPreviewBottomSheet) {
            MashupSheet(
                closeBottomShit = { isPreviewBottomSheet = false },
                sheetState = previewSheetState,
                scope = scope,
                mashupDetails = vmDetails.copy(colors = colorBuffer),
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
                },
                height = height
            )
        }
    }
}