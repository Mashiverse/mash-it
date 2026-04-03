package com.mashiverse.mashit.ui.screens.components.nft

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mashiverse.mashit.data.models.nft.Trait
import com.mashiverse.mashit.data.states.intents.ImageIntent
import com.mashiverse.mashit.ui.screens.components.nft.trait.TraitImage
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.MashiHolderHeight
import com.mashiverse.mashit.ui.theme.MashiHolderWidth
import com.mashiverse.mashit.ui.theme.Primary
import com.mashiverse.mashit.ui.theme.TraitShape

@Composable
fun TraitHolder(
    onClick: () -> Unit,
    trait: Trait,
    isSelected: Boolean = false,
    width: Dp = MashiHolderWidth,
    height: Dp = MashiHolderHeight,
    processImageIntent: (ImageIntent) -> Unit
) {
    Column(
        modifier = Modifier.width(width),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(width)
                .height(height)
                .border(
                    width = 1.dp,
                    shape = TraitShape,
                    color = if (isSelected) Primary else ContentColor
                )
                .padding(4.dp),
        ) {
            TraitImage(
                modifier = Modifier,
                data = trait.url ?: "",
                onClick = onClick,
                processImageIntent = processImageIntent
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = trait.type.name
                .lowercase()
                .replace("_", " ")
                .replaceFirstChar { c -> c.uppercaseChar() },
            color = ContentAccentColor,
            fontSize = 12.sp
        )
    }
}