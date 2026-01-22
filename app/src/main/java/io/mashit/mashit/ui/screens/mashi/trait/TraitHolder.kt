package io.mashit.mashit.ui.screens.mashi.trait

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
import io.mashit.mashit.data.models.mashi.MashiTrait
import io.mashit.mashit.ui.theme.ContentAccentColor
import io.mashit.mashit.ui.theme.ContentColor
import io.mashit.mashit.ui.theme.ExtraSmallPaddingSize
import io.mashit.mashit.ui.theme.MashiHolderHeight
import io.mashit.mashit.ui.theme.MashiHolderShape
import io.mashit.mashit.ui.theme.MashiHolderWidth

@Composable
fun TraitHolder(
    mashiTrait: MashiTrait,
    width: Dp = MashiHolderWidth,
    height: Dp = MashiHolderHeight,
) {
    Column {
        Trait(
            modifier = Modifier
                .width(width)
                .height(height)
                .border(width = 0.2.dp, shape = MashiHolderShape, color = ContentColor),
            data = mashiTrait.url,
        )

        Spacer(modifier = Modifier.height(ExtraSmallPaddingSize))

        Text(
            fontSize = 14.sp,
            text = mashiTrait.traitType.name
                .lowercase()
                .replace("_", " ")
                .replaceFirstChar { c -> c.uppercaseChar() },
            color = ContentAccentColor,
            fontWeight = FontWeight.Bold
        )
    }
}