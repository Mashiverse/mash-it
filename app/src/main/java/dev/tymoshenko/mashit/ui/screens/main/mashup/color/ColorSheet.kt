package dev.tymoshenko.mashit.ui.screens.main.mashup.color

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
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import dev.tymoshenko.mashit.data.models.color.ColorType
import dev.tymoshenko.mashit.ui.screens.main.picker.ColorPicker
import dev.tymoshenko.mashit.ui.screens.main.picker.ColorSlideBar
import dev.tymoshenko.mashit.ui.theme.*
import dev.tymoshenko.mashit.utils.color.data.Colors
import dev.tymoshenko.mashit.utils.color.helpers.ColorPickerHelper
import dev.tymoshenko.mashit.utils.color.helpers.ColorPickerHelper.toHue
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
        sheetGesturesEnabled = false
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
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
                    .height(200.dp),
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
        }
    }
}
