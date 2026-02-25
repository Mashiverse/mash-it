package com.mashiverse.mashit.ui.screens.mashup

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mashiverse.mashit.data.models.image.ImageType
import com.mashiverse.mashit.data.models.mashup.MashupTrait
import com.mashiverse.mashit.ui.components.nfts.trait.MintedTrait
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.ExtraSmallPaddingSize
import com.mashiverse.mashit.ui.theme.MashiHolderShape

@Composable
fun MashupTraitHolder(
    width: Dp,
    height: Dp,
    mashupTrait: MashupTrait,
    changeMashupTrait: (MashupTrait) -> Unit,
    getImageType: (String) -> ImageType?,
    setImageType: (ImageType, String) -> Unit,
) {
    val avatarName = mashupTrait.avatarName.substringBefore("#").trimIndent()

    Column(modifier = Modifier.width(width)) {
        MintedTrait(
            modifier = Modifier
                .width(width)
                .height(height)
                .border(width = 0.2.dp, shape = MashiHolderShape, color = ContentColor),
            onClick = { changeMashupTrait.invoke(mashupTrait) },
            data = mashupTrait.trait.url ?: "",
            getImageType = getImageType,
            setImageType = setImageType,
            mint = mashupTrait.mint
        )

        Spacer(modifier = Modifier.height(ExtraSmallPaddingSize))

        Text(
            text = avatarName,
            overflow = TextOverflow.Ellipsis, maxLines = 1,
            color = ContentAccentColor,
            fontSize = 12.sp
        )
    }
}