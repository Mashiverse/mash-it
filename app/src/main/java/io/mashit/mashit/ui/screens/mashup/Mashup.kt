package io.mashit.mashit.ui.screens.mashup

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import io.mashit.mashit.data.models.color.ColorType
import io.mashit.mashit.data.models.mashi.MashiTrait
import io.mashit.mashit.data.models.mashi.MashupTrait
import io.mashit.mashit.data.models.mashi.TraitType
import io.mashit.mashit.data.models.mashi.mappers.fromEntities
import io.mashit.mashit.data.models.wallet.WalletPreferences
import io.mashit.mashit.ui.screens.header.CategoryHeader
import io.mashit.mashit.ui.screens.mashup.color.ColorSheet
import io.mashit.mashit.ui.screens.mashup.preview.MashupPreview
import io.mashit.mashit.ui.screens.placeholder.NotConnected
import io.mashit.mashit.ui.theme.ExtraLargeMashiHolderHeight
import io.mashit.mashit.ui.theme.ExtraLargeMashiHolderWidth
import io.mashit.mashit.ui.theme.ExtraSmallPaddingSize
import io.mashit.mashit.ui.theme.PaddingSize
import io.mashit.mashit.ui.theme.SmallPaddingSize
import io.mashit.mashit.utils.color.helpers.toHexColor
import io.mashit.mashit.utils.color.helpers.toHexString
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Mashup() {
    val density = LocalDensity.current
    val ctx = LocalContext.current
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
    var colorBuffer by remember(vmDetails.colors) {
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

    val changeMashupTrait = { mashiTrait: MashiTrait -> viewModel.changeMashupTrait(mashiTrait) }

    // Collection Data
    val mashies by viewModel.collectionFlow
        .map { it.fromEntities() }
        .collectAsState(emptyList())

    val traitsByType = remember(mashies) {
        TraitType.entries.associateWith { type ->
            mashies.filter { mashie -> mashie.mashiTraits.any { it.traitType == type } }
                .map { mashie ->
                    MashupTrait(
                        mashiTrait = mashie.mashiTraits.first { it.traitType == type },
                        avatarName = mashie.name
                    )
                }
                .toSet().toList()
        }
    }

    var selectedCategory by remember { mutableStateOf(TraitType.BACKGROUND) }
    val traits by remember(selectedCategory, mashies) {
        derivedStateOf { traitsByType[selectedCategory] ?: emptyList() }
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
                                    colors = listOf(Color.Red, Color.Yellow, Color.Green, Color.Blue, Color.Red).reversed(),
                                    center = Offset(x = with(density) { 28.dp.toPx() }, y = with(density) { 16.dp.toPx() })
                                )
                            )
                            .border(0.5.dp, Color.White, RoundedCornerShape(90))
                            .clickable { isBottomSheet = true }
                    )

                    // Control Buttons
                    Column(Modifier.align(Alignment.TopEnd), horizontalAlignment = Alignment.End) {
                        Button(onClick = { viewModel.saveMashup(ctx) }) { Text("Save") }
                        Button(onClick = { viewModel.saveMashup(ctx, isStatic = false) }) { Text("Save anim") }
                        Button(
                            onClick = {
                                if (mashies.isNotEmpty()) {
                                    val randomAssets = TraitType.entries.mapNotNull { type ->
                                        traitsByType[type]?.randomOrNull()?.mashiTrait
                                    }
                                    randomAssets.forEach { viewModel.changeMashupTrait(it) }
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
                            .clickable { isPreviewBottomSheet = true },
                        holderWidth = ExtraLargeMashiHolderWidth
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
                    changeMashupTrait = changeMashupTrait
                )
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

            // MASHUP PREVIEW SHEET
            if (isPreviewBottomSheet) {
                MashupPreview(
                    closeBottomShit = { isPreviewBottomSheet = false },
                    sheetState = previewSheetState,
                    scope = scope,
                    mashupDetails = vmDetails.copy(colors = colorBuffer),
                )
            }
        } else {
            NotConnected()
        }
    }
}