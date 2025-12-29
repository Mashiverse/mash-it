package dev.tymoshenko.mashit.ui.screens.main.mashup.color

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import dev.tymoshenko.mashit.data.models.color.ColorType
import dev.tymoshenko.mashit.ui.screens.main.picker.ColorPicker
import dev.tymoshenko.mashit.ui.theme.BottomSheetShape
import dev.tymoshenko.mashit.ui.theme.ContainerColor
import dev.tymoshenko.mashit.ui.theme.ContentAccentColor
import dev.tymoshenko.mashit.ui.theme.ContentColor
import dev.tymoshenko.mashit.ui.theme.PaddingSize
import dev.tymoshenko.mashit.ui.theme.SmallPaddingSize
import kotlinx.coroutines.CoroutineScope

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorSheet(
    closeBottomShit: () -> Unit,
    sheetState: SheetState,
    scope: CoroutineScope,
    initialColor: Color,
    changeColor: (Color) -> Unit,
    selectedColorType: ColorType,
    selectColorType: (ColorType) -> Unit
) {
    val config = LocalConfiguration.current

    var color by remember {
        mutableStateOf(initialColor)
    }

    LaunchedEffect(selectedColorType) {
        color = initialColor
    }

    val changePreviewColor = { previewColor: Color ->
        color = previewColor
    }

    ModalBottomSheet(
        modifier = Modifier.fillMaxWidth(),
        shape = BottomSheetShape,
        onDismissRequest = closeBottomShit,
        sheetState = sheetState,
        containerColor = ContainerColor,
        contentColor = ContentColor,
        dragHandle = null,
        sheetGesturesEnabled = false
    ) {
        Column(
            modifier = Modifier
//                .height((config.screenHeightDp * 0.65).dp)
                .fillMaxWidth()
                .padding(start = PaddingSize, end = PaddingSize, top = SmallPaddingSize)
        ) {
            ColorTypeSelector(
                selectedColorType =selectedColorType,
                selectColorType = selectColorType
            )

            Spacer(modifier = Modifier.height(SmallPaddingSize))

            ColorPicker(
                modifier = Modifier.fillMaxWidth(), initialColor = color
            ) { newColor ->
                color = newColor
            }

            Spacer(modifier = Modifier.height(PaddingSize))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                ColorActions(
                    modifier = Modifier.weight(1F),
                    color = color,
                    changePreviewColor = changePreviewColor
                )

                Spacer(modifier = Modifier.width(PaddingSize))

                ColorPreviewSection(
                    initialColor = initialColor, updatedColor = color
                )
            }

            Spacer(modifier = Modifier.height(PaddingSize))

            ColorSheetActions(
                scope = scope,
                sheetState = sheetState,
                closeBottomShit = closeBottomShit,
                color = color,
                changeColor = changeColor
            )
        }
    }
}