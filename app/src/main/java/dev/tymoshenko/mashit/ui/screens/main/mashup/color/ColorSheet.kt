package dev.tymoshenko.mashit.ui.screens.main.mashup.color

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dev.tymoshenko.mashit.data.models.color.ColorType
import dev.tymoshenko.mashit.ui.screens.main.picker.ColorPicker
import dev.tymoshenko.mashit.ui.theme.BottomSheetShape
import dev.tymoshenko.mashit.ui.theme.ContainerColor
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
    color: Color,
    changeColor: (Color) -> Unit,
    selectedColorType: ColorType,
    selectColorType: (ColorType) -> Unit
) {
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
                modifier = Modifier.fillMaxWidth(), color = color
            ) { newColor ->
                changeColor.invoke(newColor)
            }

            Spacer(modifier = Modifier.height(PaddingSize))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                ColorActions(
                    modifier = Modifier.weight(1F),
                    color = color,
                    changePreviewColor = changeColor
                )

                Spacer(modifier = Modifier.width(PaddingSize))

                ColorPreviewSection(
                    initialColor = color, updatedColor = color
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