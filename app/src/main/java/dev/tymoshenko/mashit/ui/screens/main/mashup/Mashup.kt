package dev.tymoshenko.mashit.ui.screens.main.mashup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dev.tymoshenko.mashit.ui.screens.main.header.CategoryHeader
import dev.tymoshenko.mashit.ui.screens.main.mashi.TraitHolder
import dev.tymoshenko.mashit.ui.screens.main.mashup.color.ColorSheet
import dev.tymoshenko.mashit.ui.theme.ColorPreviewSize
import dev.tymoshenko.mashit.ui.theme.LargeMashiHolderHeight
import dev.tymoshenko.mashit.ui.theme.LargeMashiHolderWidth
import dev.tymoshenko.mashit.ui.theme.MashiHolderHeight
import dev.tymoshenko.mashit.ui.theme.MashiHolderWidth
import dev.tymoshenko.mashit.ui.theme.PaddingSize
import dev.tymoshenko.mashit.utils.color.helpers.toHexString


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Mashup() {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var isBottomSheet by remember { mutableStateOf(false) }

    var color by remember {
        mutableStateOf(Color(38, 24, 4))
    }
    val changeColor = { newColor: Color ->
        color = newColor
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

        TraitHolder(
            onClick = {
                    isBottomSheet = true
                },
            width = LargeMashiHolderWidth,
            height = LargeMashiHolderHeight,
            data = "https://ipfs.filebase.io/ipfs/QmebU67K3a2gYve4qH7haRSNQ2PZ1CN242xivunrerDEcM",
            colors = Triple("#${color.toHexString()}" ,"#${color.toHexString()}", "#${color.toHexString()}")
        )
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