package com.mashiverse.mashit.ui.screens.mashup.color

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.mashiverse.mashit.data.models.mashup.colors.ColorType
import com.mashiverse.mashit.ui.screens.components.picker.ColorPicker
import com.mashiverse.mashit.ui.screens.components.picker.ColorSlideBar
import com.mashiverse.mashit.ui.theme.*
import com.mashiverse.mashit.utils.color.data.Colors
import com.mashiverse.mashit.utils.color.helpers.ColorPickerHelper
import com.mashiverse.mashit.utils.color.helpers.ColorPickerHelper.toHue
import kotlinx.coroutines.CoroutineScope

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorSheet(
    closeBottomShit: () -> Unit,
    sheetState: SheetState,
    scope: CoroutineScope,
    color: Color,
    saveColors: () -> Unit,
    changeColor: (Color) -> Unit,
    selectedColorType: ColorType,
    selectColorType: (ColorType) -> Unit,
    height: Dp
) {
    // Picker states
    var pickerLocation by remember { mutableStateOf(Offset.Zero) }
    var rangeColor by remember { mutableStateOf(color) }
    var hueProgress by remember { mutableFloatStateOf(0f) }
    var pickerSize by remember { mutableStateOf(IntSize(1, 1)) }
    var isDragging by remember { mutableStateOf(false) }

    // Update picker gradient and thumb when color or type changes
    LaunchedEffect(color, selectedColorType, pickerSize) {
        if (!isDragging && pickerSize.width > 1 && pickerSize.height > 1) {
            val hsv = FloatArray(3)
            android.graphics.Color.colorToHSV(color.toArgb(), hsv)

            // Hue for slider
            rangeColor = ColorPickerHelper.hsvToColor(hsv[0], 1f, 1f)
            hueProgress = hsv[0] / 360f

            // Picker location (saturation, brightness)
            pickerLocation = Offset(
                x = hsv[1] * pickerSize.width,
                y = (1f - hsv[2]) * pickerSize.height
            )
        }
    }

    ModalBottomSheet(
        modifier = Modifier.fillMaxWidth(),
        shape = BottomSheetShape,
        onDismissRequest = closeBottomShit,
        sheetState = sheetState,
        containerColor = ContainerColor,
        contentColor = ContentColor,
        dragHandle = null,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .padding(start = PaddingSize, end = PaddingSize, top = SmallPaddingSize)
        ) {
            ColorTypeSelector(
                selectedColorType = selectedColorType,
                selectColorType = selectColorType
            )

            Spacer(modifier = Modifier.height(SmallPaddingSize))

            ColorPicker(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                color = color,
                rangeColor = rangeColor,
                pickerLocation = pickerLocation,
                onPickedColor = { newColor ->
                    // calculate with current hue from hueProgress
                    val hue = hueProgress * 360f
                    val hsv = FloatArray(3)
                    android.graphics.Color.colorToHSV(newColor.toArgb(), hsv)
                    val correctedColor = ColorPickerHelper.hsvToColor(hue, hsv[1], hsv[2])
                    changeColor(correctedColor)
                },
                onPickerLocationChange = { pickerLocation = it },
                onDraggingChange = { dragging -> isDragging = dragging },
                onPickerSizeChange = { pickerSize = it }
            )

            Spacer(modifier = Modifier.height(PaddingSize))

            ColorSlideBar(
                colors = Colors.gradientColors,
                progress = hueProgress,
                onProgressChange = { progress ->
                    hueProgress = progress
                    rangeColor = ColorPickerHelper.hsvToColor(progress * 360f, 1f, 1f)

                    val saturation = (pickerLocation.x / pickerSize.width).coerceIn(0f, 1f)
                    val brightness = (1f - pickerLocation.y / pickerSize.height).coerceIn(0f, 1f)
                    val newColor = ColorPickerHelper.hsvToColor(rangeColor.toHue(), saturation, brightness)
                    changeColor(newColor)
                }
            )

            Spacer(modifier = Modifier.height(PaddingSize))

            Row(verticalAlignment = Alignment.CenterVertically) {
                ColorActions(
                    modifier = Modifier.weight(1f),
                    color = color,
                    changePreviewColor = changeColor
                )

                Spacer(modifier = Modifier.width(PaddingSize))

                ColorPreviewSection(
                    initialColor = color,
                    updatedColor = color
                )
            }

            Spacer(modifier = Modifier.height(PaddingSize))

            ColorSheetActions(
                scope = scope,
                sheetState = sheetState,
                closeBottomShit = closeBottomShit,
                saveColors = saveColors
            )

            Spacer(modifier = Modifier.height(SmallPaddingSize))
        }
    }
}
