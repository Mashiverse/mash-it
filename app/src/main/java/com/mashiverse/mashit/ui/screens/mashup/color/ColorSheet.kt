package com.mashiverse.mashit.ui.screens.mashup.color

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import com.mashiverse.mashit.data.states.intents.ActionsIntent
import com.mashiverse.mashit.data.states.intents.MashupIntent
import com.mashiverse.mashit.data.models.mashup.colors.ColorType
import com.mashiverse.mashit.ui.screens.components.picker.ColorPicker
import com.mashiverse.mashit.ui.screens.components.picker.ColorSlideBar
import com.mashiverse.mashit.ui.theme.BottomSheetShape
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.Padding
import com.mashiverse.mashit.ui.theme.SmallPadding
import com.mashiverse.mashit.ui.theme.Surface
import com.mashiverse.mashit.utils.color.data.Colors
import com.mashiverse.mashit.utils.color.helpers.ColorPickerHelper
import com.mashiverse.mashit.utils.color.helpers.ColorPickerHelper.toHue
import kotlinx.coroutines.CoroutineScope

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorSheet(
    sheetState: SheetState,
    scope: CoroutineScope,
    initialColor: Color,
    color: Color,
    selectedColorType: ColorType,
    height: Dp,
    processMashupIntent: (MashupIntent) -> Unit,
    processActionsIntent: (ActionsIntent) -> Unit
) {
    // Picker states
    var pickerLocation by remember { mutableStateOf(Offset.Zero) }
    var rangeColor by remember { mutableStateOf(color) }
    var hueProgress by remember { mutableFloatStateOf(0f) }
    var pickerSize by remember { mutableStateOf(IntSize(1, 1)) }
    var isDragging by remember { mutableStateOf(false) }

    val closeBottomShit = {
        processActionsIntent(ActionsIntent.OnColorDismiss)
        processMashupIntent(MashupIntent.OnColorsReset)
    }

    val saveColors = {
        processMashupIntent(MashupIntent.OnColorsSave)
        processActionsIntent(ActionsIntent.OnColorDismiss)
    }

    val changeColor = { color: Color ->
        processMashupIntent(MashupIntent.OnColorChange(color))
    }

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
        containerColor = Surface,
        contentColor = ContentColor,
        dragHandle = null,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .padding(start = Padding, end = Padding, top = SmallPadding)
        ) {
            ColorTypeSelector(
                selectedColorType = selectedColorType,
                processMashupIntent = processMashupIntent
            )

            Spacer(modifier = Modifier.height(SmallPadding))

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
                    processMashupIntent(MashupIntent.OnColorChange(correctedColor))
                },
                onPickerLocationChange = { pickerLocation = it },
                onDraggingChange = { dragging -> isDragging = dragging },
                onPickerSizeChange = { pickerSize = it }
            )

            Spacer(modifier = Modifier.height(Padding))

            ColorSlideBar(
                colors = Colors.gradientColors,
                progress = hueProgress,
                onProgressChange = { progress ->
                    hueProgress = progress
                    rangeColor = ColorPickerHelper.hsvToColor(progress * 360f, 1f, 1f)

                    val saturation = (pickerLocation.x / pickerSize.width).coerceIn(0f, 1f)
                    val brightness = (1f - pickerLocation.y / pickerSize.height).coerceIn(0f, 1f)
                    val newColor =
                        ColorPickerHelper.hsvToColor(rangeColor.toHue(), saturation, brightness)
                    processMashupIntent(MashupIntent.OnColorChange(newColor))
                }
            )

            Spacer(modifier = Modifier.height(Padding))

            Row(verticalAlignment = Alignment.CenterVertically) {
                ColorActions(
                    modifier = Modifier.weight(1f),
                    color = color,
                    changePreviewColor = changeColor
                )

                Spacer(modifier = Modifier.width(Padding))

                ColorPreviewSection(
                    initialColor = initialColor,
                    updatedColor = color
                )
            }

            Spacer(modifier = Modifier.height(Padding))

            ColorSheetActions(
                scope = scope,
                sheetState = sheetState,
                closeBottomShit = closeBottomShit,
                saveColors = saveColors
            )

            Spacer(modifier = Modifier.height(SmallPadding))
        }
    }
}
