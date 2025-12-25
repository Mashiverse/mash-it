package dev.tymoshenko.mashit.ui.screens.main.shop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.tymoshenko.mashit.ui.screens.main.components.MashiHolder
import dev.tymoshenko.mashit.ui.theme.ContentColor
import dev.tymoshenko.mashit.ui.theme.ContentTextSize
import dev.tymoshenko.mashit.ui.theme.ExtraSmallPaddingSize
import dev.tymoshenko.mashit.ui.theme.SearchBarBackground

@Composable
fun ShopItem(
    i: Int,
    avatarName: String = "Ervindas",
    authorName: String = "Ervindas",
    soldQuantity: Int = 10,
    quantity: Int = 100
) {
    Column(
        modifier = Modifier,
    ) {
        MashiHolder()

        Spacer(modifier = Modifier.height(ExtraSmallPaddingSize))

        Text(text = avatarName, fontSize = 14.sp, color = Color.White)

        Text(text = "by $authorName", fontSize = 12.sp, color = ContentColor)

        Text(text = "$soldQuantity of $quantity sold", fontSize = 12.sp, color = ContentColor)

    }
}

@Preview
@Composable
private fun ShopItemPreview() {
    ShopItem(1)
}