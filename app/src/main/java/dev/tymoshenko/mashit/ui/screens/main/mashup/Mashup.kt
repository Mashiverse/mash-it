package dev.tymoshenko.mashit.ui.screens.main.mashup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
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
import dev.tymoshenko.mashit.utils.color.helpers.toHexString


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Mashup() {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var isBottomSheet by remember { mutableStateOf(false) }

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

    val mashupTraits by remember {
        viewModel.mashupDetails
    }

    val mashies by remember {
        viewModel.mashies
    }

    val traits by remember(mashupTraits) {
        derivedStateOf {
            listOf(
                mashupTraits.background,
                mashupTraits.hairBack,
                mashupTraits.cape,
                mashupTraits.bottom,
                mashupTraits.upper,
                mashupTraits.head,
                mashupTraits.eyes,
                mashupTraits.hairFront,
                mashupTraits.hat,
                mashupTraits.leftAccessory,
                mashupTraits.rightAccessory
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
                trait = it.traits.first { it.traitType == TraitType.CAPE },
                avatarName = it.name
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
                trait = it.traits.first { it.traitType == TraitType.BOTTOM },
                avatarName = it.name
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
                trait = it.traits.first { it.traitType == TraitType.UPPER },
                avatarName = it.name
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
                trait = it.traits.first { it.traitType == TraitType.HEAD },
                avatarName = it.name
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
                trait = it.traits.first { it.traitType == TraitType.EYES },
                avatarName = it.name
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
                trait = it.traits.first { it.traitType == TraitType.HAT },
                avatarName = it.name
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = PaddingSize)
    ) {
        CategoryHeader(title = "Mashup")

        Box(
            modifier = Modifier
                .height(LargeMashiHolderHeight)
                .width(LargeMashiHolderWidth)
                .clip(MashiHolderShape)
                .background(MashiBackground)
        ) {
            CompositeHolder(
                modifier = Modifier
                    .fillMaxSize(),
                traits = traits,
                bodyColor = "#${body.value.toHexString()}",
                eyesColor = "#${eyes.value.toHexString()}",
                hairColor = "#${hair.value.toHexString()}"
            )
        }

        Column {
            Button(
                modifier = Modifier.background(body.value),
                onClick = {
                    selectColorType.invoke(ColorType.BODY)
                    isBottomSheet = true
                }) {
                Text("Body")
            }

            Button(
                modifier = Modifier.background(eyes.value),
                onClick = {
                    selectColorType.invoke(ColorType.EYES)
                    isBottomSheet = true
                }) {
                Text("Eyes")
            }

            Button(modifier = Modifier.background(hair.value), onClick = {
                selectColorType.invoke(ColorType.HAIR)
                isBottomSheet = true
            }) {
                Text("Hair")
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(backgroundTraits.size) { i ->
                    TraitHolder(
                        onClick = { changeMashupTrait.invoke(backgroundTraits[i].trait) },
                        data = backgroundTraits[i].trait.url
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