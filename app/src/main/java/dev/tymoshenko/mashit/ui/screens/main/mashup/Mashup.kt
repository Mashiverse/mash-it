package dev.tymoshenko.mashit.ui.screens.main.mashup

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dev.tymoshenko.mashit.data.models.color.ColorType
import dev.tymoshenko.mashit.data.models.color.SelectedColors
import dev.tymoshenko.mashit.data.models.mashi.MashupTrait
import dev.tymoshenko.mashit.data.models.mashi.Trait
import dev.tymoshenko.mashit.data.models.mashi.TraitType
import dev.tymoshenko.mashit.ui.screens.main.header.CategoryHeader
import dev.tymoshenko.mashit.ui.screens.main.mashup.color.ColorSheet
import dev.tymoshenko.mashit.ui.screens.main.mashup.composite.CompositeHolder
import dev.tymoshenko.mashit.ui.theme.ContentAccentColor
import dev.tymoshenko.mashit.ui.theme.ContentColor
import dev.tymoshenko.mashit.ui.theme.LargeMashiHolderHeight
import dev.tymoshenko.mashit.ui.theme.LargeMashiHolderWidth
import dev.tymoshenko.mashit.ui.theme.MashiBackground
import dev.tymoshenko.mashit.ui.theme.MashiHolderShape
import dev.tymoshenko.mashit.ui.theme.PaddingSize
import dev.tymoshenko.mashit.ui.theme.SmallPaddingSize
import dev.tymoshenko.mashit.utils.color.helpers.toHexString
import kotlinx.coroutines.launch


@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Mashup() {
    val ctx = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var isBottomSheet by remember { mutableStateOf(false) }
    val closeBottomShit = { isBottomSheet = false }

    val lazyGridState = rememberLazyGridState()

    val config = LocalConfiguration.current
    val mashiHolderWidth = (config.screenWidthDp.dp - 2 * PaddingSize - 2 * SmallPaddingSize) / 3
    val mashiHolderHeight = mashiHolderWidth * 4 / 3

    val viewModel = hiltViewModel<MashupViewModel>()

    // Colors
    val body = remember { viewModel.body }
    val eyes = remember { viewModel.eyes }
    val hair = remember { viewModel.hair }
    val selectedColorType = remember { viewModel.selectedColorType }
    val selectColorType = { colorType: ColorType -> viewModel.selectColorType(colorType) }
    val changeColor =
        { newColor: Color -> viewModel.changeColor(newColor, selectedColorType.value) }

    val color by remember(selectedColorType, body.value, eyes.value, hair.value) {
        derivedStateOf {
            when (selectedColorType.value) {
                ColorType.BODY -> body.value
                ColorType.EYES -> eyes.value
                ColorType.HAIR -> hair.value
            }
        }
    }

    // Mashup
    val mashupDetail by remember { viewModel.mashupDetails }
    val mashupTraits by remember(mashupDetail) {
        derivedStateOf {
            listOf(
                mashupDetail.background,
                mashupDetail.hairBack,
                mashupDetail.cape,
                mashupDetail.bottom,
                mashupDetail.upper,
                mashupDetail.head,
                mashupDetail.eyes,
                mashupDetail.hairFront,
                mashupDetail.hat,
                mashupDetail.leftAccessory,
                mashupDetail.rightAccessory
            )
        }
    }
    val changeMashupTrait = { trait: Trait -> viewModel.changeMashupTrait(trait) }


    // traits
    val mashies by remember { viewModel.mashies }
    val traitsByType = remember(mashies) {
        TraitType.entries.associateWith { type ->
            mashies.filter { mashie ->
                mashie.traits.any { it.traitType == type }
            }.map { mashie ->
                MashupTrait(
                    trait = mashie.traits.first { it.traitType == type },
                    avatarName = mashie.name
                )
            }.toSet().toList()
        }
    }
    var selectedCategory by remember { mutableStateOf(TraitType.BACKGROUND) }
    val traits by remember(selectedCategory, mashies) {
        derivedStateOf {
            traitsByType[selectedCategory] ?: emptyList()
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = PaddingSize)
    ) {
        CategoryHeader(title = "Mashup")

        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                modifier = Modifier
                    .align(Alignment.TopStart),
                onClick = {
                    isBottomSheet = true
                }
            ) {
                Text("COLOR")
            }

            Column(Modifier.align(Alignment.TopEnd)) {
                Button(
                    onClick = {
                        viewModel.saveMashup(ctx)
                    }
                ) {
                    Text("Save")
                }

                Button(
                    onClick = {
                        viewModel.saveMashup(ctx, isStatic = false)
                    }
                ) {
                    Text("Save anim")
                }
            }

            Box(
                modifier = Modifier
                    .height(LargeMashiHolderHeight)
                    .width(LargeMashiHolderWidth)
                    .clip(MashiHolderShape)
                    .background(MashiBackground)
            ) {
                CompositeHolder(
                    modifier = Modifier.fillMaxSize(),
                    traits = mashupTraits,
                    selectedColors = SelectedColors(
                        body = "#${body.value.toHexString()}",
                        eyes = "#${eyes.value.toHexString()}",
                        hair = "#${hair.value.toHexString()}"
                    ),
                )
            }
        }

        Spacer(Modifier.height(SmallPaddingSize))

        LazyRow {
            items(TraitType.entries) { traitType ->
                TextButton(
                    onClick = {
                        selectedCategory = traitType
                        scope.launch {
                            lazyGridState.scrollToItem(0)
                        }
                    }
                ) {
                    Text(
                        text = traitType.name
                            .lowercase()
                            .replace("_", " "),
                        fontSize = 14.sp,
                        color = if (traitType == selectedCategory) ContentAccentColor else ContentColor
                    )
                }
            }
        }

        Spacer(Modifier.height(SmallPaddingSize))

        LazyVerticalGrid(
            modifier = Modifier.fillMaxWidth(),
            state = lazyGridState,
            verticalArrangement = Arrangement.spacedBy(PaddingSize),
            horizontalArrangement = Arrangement.spacedBy(SmallPaddingSize),
            columns = GridCells.Fixed(3)
        ) {
            items(traits.size) { i ->
                MashupTraitHolder(
                    mashiHolderHeight = mashiHolderHeight,
                    mashiHolderWidth = mashiHolderWidth,
                    trait = traits[i],
                    changeMashupTrait = changeMashupTrait
                )
            }
        }
    }

    if (isBottomSheet) {
        ColorSheet(
            closeBottomShit = closeBottomShit,
            scope = scope,
            sheetState = sheetState,
            initialColor = color,
            changeColor = changeColor,
            selectColorType = selectColorType,
            selectedColorType = selectedColorType.value
        )
    }
}