package dev.tymoshenko.mashit.ui.screens.main.mashup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dev.tymoshenko.mashit.data.models.color.ColorType
import dev.tymoshenko.mashit.data.models.mashi.TraitType
import dev.tymoshenko.mashit.data.models.mashi.rinniTraitsExample
import dev.tymoshenko.mashit.ui.screens.main.header.CategoryHeader
import dev.tymoshenko.mashit.ui.screens.main.mashi.TraitHolder
import dev.tymoshenko.mashit.ui.screens.main.mashup.color.ColorSheet
import dev.tymoshenko.mashit.ui.theme.ColorPreviewSize
import dev.tymoshenko.mashit.ui.theme.LargeMashiHolderHeight
import dev.tymoshenko.mashit.ui.theme.LargeMashiHolderWidth
import dev.tymoshenko.mashit.ui.theme.LargeTraitHolderWidth
import dev.tymoshenko.mashit.ui.theme.MashiHolderHeight
import dev.tymoshenko.mashit.ui.theme.MashiHolderWidth
import dev.tymoshenko.mashit.ui.theme.PaddingSize
import dev.tymoshenko.mashit.utils.color.helpers.toHexString
import retrofit2.http.Body


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
            .width(LargeMashiHolderWidth),
            contentAlignment = Alignment.Center
        ) {
            rinniTraitsExample.forEach { trait ->
                if (trait.traitType == TraitType.BACKGROUND) {
                    TraitHolder(
                        onClick = {
                            isBottomSheet = true
                        },
                        width = LargeMashiHolderWidth,
                        height = LargeMashiHolderHeight,
                        data = trait.url,
                        colors = Triple("#${body.value.toHexString()}" ,"#${eyes.value.toHexString()}", "#${hair.value.toHexString()}")
                    )
                } else {
                    TraitHolder(
                        onClick = {
                            isBottomSheet = true
                        },
                        width = LargeTraitHolderWidth,
                        height = LargeMashiHolderHeight,
                        data = trait.url,
                        colors = Triple("#${body.value.toHexString()}" ,"#${eyes.value.toHexString()}", "#${hair.value.toHexString()}"),
                        background = Color.Transparent
                    )
                }
            }
        }

        Column {
            Button(modifier = Modifier.background(body.value),onClick = {selectColorType.invoke(ColorType.BODY)}) {
                Text("Body")
            }

            Button(modifier = Modifier.background(eyes.value), onClick = {selectColorType.invoke(ColorType.EYES)}) {
                Text("Eyes")
            }

            Button(modifier = Modifier.background(hair.value),onClick = {selectColorType.invoke(ColorType.HAIR)}) {
                Text("Hair")
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