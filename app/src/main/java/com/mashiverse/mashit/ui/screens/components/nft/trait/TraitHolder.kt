package com.mashiverse.mashit.ui.screens.components.nft.trait

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mashiverse.mashit.data.models.image.ImageType
import com.mashiverse.mashit.data.models.nft.Trait
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.ExtraSmallPaddingSize
import com.mashiverse.mashit.ui.theme.MashiHolderHeight
import com.mashiverse.mashit.ui.theme.MashiHolderShape
import com.mashiverse.mashit.ui.theme.MashiHolderWidth

@Composable
fun TraitHolder(
    trait: Trait,
    width: Dp = MashiHolderWidth,
    height: Dp = MashiHolderHeight,
    getImageType: (String) -> ImageType?,
    setImageType: (ImageType, String) -> Unit,
) {
    Column {
        TraitImage(
            modifier = Modifier
                .width(width)
                .height(height)
                .border(width = 0.2.dp, shape = MashiHolderShape, color = ContentColor),
            data = trait.url ?: "",
            getImageType = getImageType,
            setImageType = setImageType
        )

        Spacer(modifier = Modifier.height(ExtraSmallPaddingSize))

        Text(
            fontSize = 14.sp,
            text = trait.type.name
                .lowercase()
                .replace("_", " ")
                .replaceFirstChar { c -> c.uppercaseChar() },
            color = ContentAccentColor,
            fontWeight = FontWeight.Bold
        )
    }
}