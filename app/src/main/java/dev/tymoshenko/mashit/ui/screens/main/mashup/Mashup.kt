package dev.tymoshenko.mashit.ui.screens.main.mashup

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dev.tymoshenko.mashit.data.models.color.ColorType
import dev.tymoshenko.mashit.data.models.mashi.MashupTrait
import dev.tymoshenko.mashit.data.models.mashi.Trait
import dev.tymoshenko.mashit.data.models.mashi.TraitType
import dev.tymoshenko.mashit.ui.screens.main.header.CategoryHeader
import dev.tymoshenko.mashit.ui.screens.main.mashi.TraitHolder
import dev.tymoshenko.mashit.ui.screens.main.mashup.color.ColorSheet
import dev.tymoshenko.mashit.ui.screens.main.mashup.composite.CompositeHolder
import dev.tymoshenko.mashit.ui.theme.LargeMashiHolderHeight
import dev.tymoshenko.mashit.ui.theme.LargeMashiHolderWidth
import dev.tymoshenko.mashit.ui.theme.MashiBackground
import dev.tymoshenko.mashit.ui.theme.MashiHolderShape
import dev.tymoshenko.mashit.ui.theme.PaddingSize
import dev.tymoshenko.mashit.ui.theme.SmallPaddingSize
import dev.tymoshenko.mashit.utils.color.helpers.toHexString
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Mashup() {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var isBottomSheet by remember { mutableStateOf(false) }

    val lazyGridState = rememberLazyGridState()

    val config = LocalConfiguration.current
    val mashiHolderWidth = (config.screenWidthDp.dp - 2 * PaddingSize - 2 * SmallPaddingSize) / 3
    val mashiHolderHeight = mashiHolderWidth * 4 / 3

    val viewModel = hiltViewModel<MashupViewModel>()

    val body = remember {
        viewModel.body
    }

    val eyes = remember {
        viewModel.eyes
    }

    val hair = remember {
        viewModel.hair
    }

    val selectedColorType = remember {
        viewModel.selectedColorType
    }

    val selectColorType = { colorType: ColorType ->
        viewModel.selectColorType(colorType)
    }

    val mashupDetail by remember {
        viewModel.mashupDetails
    }

    val mashies by remember {
        viewModel.mashies
    }

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

    val changeMashupTrait = { trait: Trait ->
        viewModel.changeMashupTrait(trait)
    }

    val backgroundTraits = remember(mashies) {
        mashies.filter { mashie ->
            mashie.traits.any { trait ->
                trait.traitType == TraitType.BACKGROUND
            }
        }.map {
            MashupTrait(
                trait = it.traits.first { it.traitType == TraitType.BACKGROUND },
                avatarName = it.name
            )
        }
    }
    val hairBackTraits = remember(mashies) {
        mashies.filter { mashie ->
            mashie.traits.any { trait ->
                trait.traitType == TraitType.HAIR_BACK
            }
        }.map {
            MashupTrait(
                trait = it.traits.first { it.traitType == TraitType.HAIR_BACK },
                avatarName = it.name
            )
        }
    }
    val capeTraits = remember(mashies) {
        mashies.filter { mashie ->
            mashie.traits.any { trait ->
                trait.traitType == TraitType.CAPE
            }
        }.map {
            MashupTrait(
                trait = it.traits.first { it.traitType == TraitType.CAPE }, avatarName = it.name
            )
        }
    }
    val bottomTraits = remember(mashies) {
        mashies.filter { mashie ->
            mashie.traits.any { trait ->
                trait.traitType == TraitType.BOTTOM
            }
        }.map {
            MashupTrait(
                trait = it.traits.first { it.traitType == TraitType.BOTTOM }, avatarName = it.name
            )
        }
    }
    val upperTraits = remember(mashies) {
        mashies.filter { mashie ->
            mashie.traits.any { trait ->
                trait.traitType == TraitType.UPPER
            }
        }.map {
            MashupTrait(
                trait = it.traits.first { it.traitType == TraitType.UPPER }, avatarName = it.name
            )
        }
    }
    val headTraits = remember(mashies) {
        mashies.filter { mashie ->
            mashie.traits.any { trait ->
                trait.traitType == TraitType.HEAD
            }
        }.map {
            MashupTrait(
                trait = it.traits.first { it.traitType == TraitType.HEAD }, avatarName = it.name
            )
        }
    }
    val eyesTraits = remember(mashies) {
        mashies.filter { mashie ->
            mashie.traits.any { trait ->
                trait.traitType == TraitType.EYES
            }
        }.map {
            MashupTrait(
                trait = it.traits.first { it.traitType == TraitType.EYES }, avatarName = it.name
            )
        }
    }
    val hairFrontTraits = remember(mashies) {
        mashies.filter { mashie ->
            mashie.traits.any { trait ->
                trait.traitType == TraitType.HAIR_FRONT
            }
        }.map {
            MashupTrait(
                trait = it.traits.first { it.traitType == TraitType.HAIR_FRONT },
                avatarName = it.name
            )
        }
    }
    val hatTraits = remember(mashies) {
        mashies.filter { mashie ->
            mashie.traits.any { trait ->
                trait.traitType == TraitType.HAT
            }
        }.map {
            MashupTrait(
                trait = it.traits.first { it.traitType == TraitType.HAT }, avatarName = it.name
            )
        }
    }
    val leftTraits = remember(mashies) {
        mashies.filter { mashie ->
            mashie.traits.any { trait ->
                trait.traitType == TraitType.LEFT_ACCESSORY
            }
        }.map {
            MashupTrait(
                trait = it.traits.first { it.traitType == TraitType.LEFT_ACCESSORY },
                avatarName = it.name
            )
        }
    }
    val rightTraits = remember(mashies) {
        mashies.filter { mashie ->
            mashie.traits.any { trait ->
                trait.traitType == TraitType.RIGHT_ACCESSORY
            }
        }.map {
            MashupTrait(
                trait = it.traits.first { it.traitType == TraitType.RIGHT_ACCESSORY },
                avatarName = it.name
            )
        }
    }

    var selectedCategory by remember {
        mutableStateOf(TraitType.BACKGROUND)
    }

    val traits by remember(selectedCategory, mashies) {
        derivedStateOf {
            when (selectedCategory) {
                TraitType.BACKGROUND -> backgroundTraits
                TraitType.HAIR_BACK -> hairBackTraits
                TraitType.CAPE -> capeTraits
                TraitType.BOTTOM -> bottomTraits
                TraitType.UPPER -> upperTraits
                TraitType.HEAD -> headTraits
                TraitType.EYES -> eyesTraits
                TraitType.HAIR_FRONT -> hairFrontTraits
                TraitType.HAT -> hatTraits
                TraitType.LEFT_ACCESSORY -> leftTraits
                TraitType.RIGHT_ACCESSORY -> rightTraits
            }
        }
    }

    val changeColor = remember(selectedColorType.value) {
        { newColor: Color ->
            when (selectedColorType.value) {
                ColorType.BODY -> viewModel.changeBody(newColor)
                ColorType.EYES -> viewModel.changeEyes(newColor)
                ColorType.HAIR -> viewModel.changeHair(newColor)
            }
        }
    }

    val color by remember(selectedColorType, body.value, eyes.value, hair.value) {
        derivedStateOf {
            when (selectedColorType.value) {
                ColorType.BODY -> body.value
                ColorType.EYES -> eyes.value
                ColorType.HAIR -> hair.value
            }
        }
    }


    val closeBottomShit = {
        isBottomSheet = false
    }

    Log.d("COLL", backgroundTraits.size.toString())
    Log.d("COLL", traits.size.toString())

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
                    bodyColor = "#${body.value.toHexString()}",
                    eyesColor = "#${eyes.value.toHexString()}",
                    hairColor = "#${hair.value.toHexString()}"
                )
            }
        }

        Column {
            LazyRow {
                items(TraitType.entries) { traitType ->
                    Button(
                        onClick = {
                            when (traitType) {
                                TraitType.EYES -> {
                                    selectColorType(ColorType.EYES)
                                    selectedCategory = traitType
                                    scope.launch {
                                        lazyGridState.scrollToItem(0)
                                    }
                                }

                                TraitType.HAIR_BACK, TraitType.HAIR_FRONT -> {
                                    selectColorType(ColorType.HAIR)
                                    selectedCategory = traitType
                                    scope.launch {
                                        lazyGridState.scrollToItem(0)
                                    }
                                }

                                else -> {
                                    selectColorType(ColorType.BODY)
                                    selectedCategory = traitType
                                    scope.launch {
                                        lazyGridState.scrollToItem(0)
                                    }
                                }
                            }
                        }
                    ) {
                        Text(traitType.name)
                    }
                }
            }

            LazyVerticalGrid(
                modifier = Modifier.fillMaxWidth(),
                state =lazyGridState,
                verticalArrangement = Arrangement.spacedBy(SmallPaddingSize),
                horizontalArrangement = Arrangement.spacedBy(SmallPaddingSize),
                columns = GridCells.Fixed(3)
            ) {
                items(traits.size) { i ->
                    TraitHolder(
                        width = mashiHolderWidth,
                        height = mashiHolderHeight,
                        onClick = { changeMashupTrait.invoke(traits[i].trait) },
                        data = traits[i].trait.url,
                        colors = Triple(
                            "#${body.value.toHexString()}",
                            "#${eyes.value.toHexString()}",
                            "#${hair.value.toHexString()}"
                        )
                    )
                }
            }
        }
    }

    if (isBottomSheet) {
        ColorSheet(
            closeBottomShit = closeBottomShit,
            scope = scope,
            sheetState = sheetState,
            initialColor = color,
            changeColor = changeColor
        )
    }
}