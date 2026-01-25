package com.mashiverse.mashit.ui.screens.mashi.trait

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mashiverse.mashit.data.models.image.ImageType
import com.mashiverse.mashit.data.models.mashi.MashupTrait
import com.mashiverse.mashit.data.models.mashi.MashiTrait
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.ExtraSmallPaddingSize
import com.mashiverse.mashit.ui.theme.MashiHolderShape

@Composable
fun MashupTraitHolder(
    mashiHolderWidth: Dp,
    mashiHolderHeight: Dp,
    trait: MashupTrait,
    changeMashupTrait: (MashiTrait) -> Unit,
    getImageType: (String) -> ImageType?,
    setImageType: (ImageType, String) -> Unit,
) {
    var avatarName = trait.avatarName.substringBefore("#").trimIndent()
    avatarName = if (avatarName.length > 16) {
        avatarName.take(13) + "..."
    } else {
        avatarName
    }

    Column(
        modifier = Modifier
            .width(mashiHolderWidth)
    ) {
        Trait(
            modifier = Modifier
                .width(mashiHolderWidth)
                .height(mashiHolderHeight)
                .border(width = 0.2.dp, shape = MashiHolderShape, color = ContentColor),
            onClick = { changeMashupTrait.invoke(trait.mashiTrait) },
            data = trait.mashiTrait.url,
            getImageType = getImageType,
            setImageType = setImageType
        )

        Spacer(modifier = Modifier.height(ExtraSmallPaddingSize))

        Text(
            text = avatarName,
            color = ContentAccentColor,
            fontSize = 12.sp
        )
    }
}