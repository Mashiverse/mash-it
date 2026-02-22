package com.mashiverse.mashit.ui.screens.mashup.actions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.mashiverse.mashit.data.models.image.ImageType
import com.mashiverse.mashit.data.models.mashup.MashupDetails

@Composable
fun MashupActions(
    mashupDetails: MashupDetails,
    modifier: Modifier = Modifier,
    getImageType: (String) -> ImageType?,
    setImageType: (ImageType, String) -> Unit,
    holderWidth: Dp,
    onColorButtonClick: () -> Unit,
    onRandomButtonClick: () -> Unit,
    onSaveButtonClick: () -> Unit,
    onPngButtonClick: () -> Unit,
    onGifButtonClick: () -> Unit,
    onResetButtonClick: () -> Unit,
    onRedoButtonClick: () -> Unit,
    onPreviewButtonClick: () -> Unit,
    onUndoButtonClick: () -> Unit,
    canUndo: State<Boolean>,
    canRedo: State<Boolean>
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row {
            LeftPanel(
                onColorButtonClick = onColorButtonClick,
                onRandomButtonClick = onRandomButtonClick,
                onResetButtonClick = onResetButtonClick,
                onPreviewButtonClick = onPreviewButtonClick
            )

            Spacer(modifier = Modifier.weight(1F))

            MashupComposite(
                modifier = modifier,
                mashupDetails = mashupDetails,
                getImageType = getImageType,
                setImageType = setImageType,
                holderWidth = holderWidth,
            )

            Spacer(modifier = Modifier.weight(1F))

            RightPanel(
                onSaveButtonClick = onSaveButtonClick,
                onPngButtonClick = onPngButtonClick,
                onGifButtonClick = onGifButtonClick,
                onRedoButtonClick = onRedoButtonClick,
                onUndoButtonClick = onUndoButtonClick,
                canUndo = canUndo,
                canRedo = canRedo
            )
        }
    }
}