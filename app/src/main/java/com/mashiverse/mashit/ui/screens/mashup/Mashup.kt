package com.mashiverse.mashit.ui.screens.mashup

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.mashiverse.mashit.data.models.color.ColorType
import com.mashiverse.mashit.data.models.image.ImageType
import com.mashiverse.mashit.data.models.mashi.MashupTrait
import com.mashiverse.mashit.data.models.mashi.Nft
import com.mashiverse.mashit.data.models.mashi.Owned
import com.mashiverse.mashit.data.models.mashi.TraitType
import com.mashiverse.mashit.data.models.mashi.mappers.fromEntities
import com.mashiverse.mashit.data.models.wallet.WalletPreferences
import com.mashiverse.mashit.ui.screens.components.header.CategoryHeader
import com.mashiverse.mashit.ui.screens.components.placeholder.NotConnected
import com.mashiverse.mashit.ui.screens.mashup.color.ColorSheet
import com.mashiverse.mashit.ui.screens.mashup.preview.MashupPreview
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.ExtraLargeMashiHolderHeight
import com.mashiverse.mashit.ui.theme.ExtraLargeMashiHolderWidth
import com.mashiverse.mashit.ui.theme.ExtraSmallPaddingSize
import com.mashiverse.mashit.ui.theme.MashiHolderShape
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
fun Mashup() {
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

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

    val changeMashupTrait = { mashupTrait: MashupTrait, isRandom: Boolean ->
        viewModel.updateMashup(mashupTrait, isRandom)
    }

    // Collection Data
    val collection by viewModel.collectionFlow
        .map { it.fromEntities() }
        .collectAsState(emptyList())

    var nfts by remember { mutableStateOf<List<Nft>>(emptyList()) }

    LaunchedEffect(collection) {
        val temp = mutableListOf<Nft>()
        Timber.tag("GG").d(collection.size.toString())
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
        nfts = temp
    }

    val traitsByType = remember(nfts) {
        Timber.tag("GG").d(nfts.size.toString())
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

    Column {
        CategoryHeader(title = "Mashup")
        Spacer(modifier = Modifier.height(ExtraSmallPaddingSize))

        if (walletPreferences.value.wallet != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = PaddingSize)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    // Color Picker Trigger
                    Box(
                        modifier = Modifier
                            .width(56.dp)
                            .height(32.dp)
                            .align(Alignment.TopStart)
                            .clip(RoundedCornerShape(90))
                            .background(
                                brush = Brush.sweepGradient(
                                    colors = listOf(
                                        Color.Red,
                                        Color.Yellow,
                                        Color.Green,
                                        Color.Blue,
                                        Color.Red
                                    ).reversed(),
                                    center = Offset(
                                        x = with(density) { 28.dp.toPx() },
                                        y = with(density) { 16.dp.toPx() })
                                )
                            )
                            .border(0.5.dp, Color.White, RoundedCornerShape(90))
                            .clickable { isBottomSheet = true }
                    )

                    // Control Buttons
                    Column(Modifier.align(Alignment.TopEnd), horizontalAlignment = Alignment.End) {
                        Button(
                            onClick = {
                                if (nfts.isNotEmpty()) {
                                    val randomAssets = TraitType.entries.mapNotNull { type ->
                                        traitsByType[type]?.randomOrNull()
                                    }
                                    randomAssets.forEach { asset ->
                                        changeMashupTrait.invoke(asset, true)
                                    }
                                }
                            }
                        ) { Text("R") }
                    }

                    // MASHUP PREVIEW: We use vmDetails but pass colorBuffer for live feedback
                    MashupComposite(
                        mashupDetails = vmDetails.copy(colors = colorBuffer),
                        modifier = Modifier
                            .height(ExtraLargeMashiHolderHeight)
                            .width(ExtraLargeMashiHolderWidth)
                            .clickable { isPreviewBottomSheet = true }
                            .border(width = 0.4.dp, shape = MashiHolderShape, color = ContentColor),
                        holderWidth = ExtraLargeMashiHolderWidth,
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

                Spacer(Modifier.height(SmallPaddingSize))

                MashupCategories(
                    onMashupCategorySelect = onMashupCategorySelect,
                    selectedCategory = selectedCategory
                )

                Spacer(Modifier.height(SmallPaddingSize))

                MashupCategoryItems(
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
            )
        }

        if (isPreviewBottomSheet) {
            MashupPreview(
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
                }
            )
        }
    }
}