package com.mashiverse.mashit.ui.screens.mashup.color

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mashiverse.mashit.data.intents.MashupIntent
import com.mashiverse.mashit.data.models.mashup.colors.ColorType
import com.mashiverse.mashit.ui.theme.Secondary
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.InactiveMashupButtonBackground
import com.mashiverse.mashit.ui.theme.SmallPaddingSize

@Composable
fun ColorTypeButton(
    text: String,
    selectedColorType: ColorType,
    colorType: ColorType,
    processMashupIntent: (MashupIntent) -> Unit
) {
    Button(
        modifier = Modifier
            .height(36.dp),
        onClick = {
            processMashupIntent(MashupIntent.OnColorTypeSelect(colorType))
        },
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = if (selectedColorType == colorType) {
                Secondary
            } else {
                InactiveMashupButtonBackground
            },
            contentColor = if (selectedColorType == colorType) ContentAccentColor else ContentColor
        )
    ) {
        Text(
            text = text,
        )
    }
}

@Composable
fun ColorTypeSelector(
    processMashupIntent: (MashupIntent) -> Unit,
    selectedColorType: ColorType
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Spacer(Modifier.weight(1f))

        ColorTypeButton(
            text = "Body",
            selectedColorType = selectedColorType,
            processMashupIntent = processMashupIntent,
            colorType = ColorType.BASE
        )

        Spacer(Modifier.width(SmallPaddingSize))

        ColorTypeButton(
            text = "Eyes",
            selectedColorType = selectedColorType,
            processMashupIntent = processMashupIntent,
            colorType = ColorType.EYES
        )

        Spacer(Modifier.width(SmallPaddingSize))

        ColorTypeButton(
            text = "Hair",
            selectedColorType = selectedColorType,
            processMashupIntent = processMashupIntent,
            colorType = ColorType.HAIR
        )

        Spacer(Modifier.weight(1f))
    }
}