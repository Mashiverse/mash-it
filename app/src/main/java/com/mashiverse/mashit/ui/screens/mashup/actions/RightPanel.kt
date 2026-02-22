package com.mashiverse.mashit.ui.screens.mashup.actions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Redo
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.mashiverse.mashit.ui.screens.mashup.actions.buttons.ActionButton
import com.mashiverse.mashit.ui.screens.mashup.actions.buttons.SaveActionButton
import com.mashiverse.mashit.ui.theme.ColorSaveButtonBackground
import com.mashiverse.mashit.ui.theme.ContainerColor
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.PaddingSize
import com.mashiverse.mashit.ui.theme.SmallPaddingSize

@Composable
fun RightPanel(
    onPngButtonClick: () -> Unit,
    onGifButtonClick: () -> Unit,
    onSaveButtonClick: () -> Unit
) {
    val commonModifier = Modifier
        .width(56.dp)
        .height(38.dp)

    Column {
        Spacer(modifier = Modifier.height(SmallPaddingSize))

        SaveActionButton(onSaveButtonClick = onSaveButtonClick)

        Spacer(modifier = Modifier.height(3 * SmallPaddingSize))

        ActionButton(
            icon = Icons.Default.Download,
            onClick = onPngButtonClick
        )

        Spacer(modifier = Modifier.height(SmallPaddingSize))

        ActionButton(
            icon = Icons.Default.Download,
            onClick = onGifButtonClick,
            isAnimated = true
        )

        Spacer(modifier = Modifier.height(SmallPaddingSize))

        ActionButton(
            icon = Icons.Default.Redo,
            onClick = {},
        )

        Spacer(modifier = Modifier.height(SmallPaddingSize))

        ActionButton(
            icon = Icons.Default.Undo,
            onClick = {},
        )
    }
}