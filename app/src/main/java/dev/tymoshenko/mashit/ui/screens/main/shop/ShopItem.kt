package dev.tymoshenko.mashit.ui.screens.main.shop

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import dev.tymoshenko.mashit.ui.screens.main.buttons.BuyButton
import dev.tymoshenko.mashit.ui.screens.main.mashi.MashiHolder
import dev.tymoshenko.mashit.ui.theme.ContentColor
import dev.tymoshenko.mashit.ui.theme.ExtraSmallPaddingSize

@Composable
fun ShopItem(
    i: Int,
    avatarName: String = "Ervindas",
    authorName: String = "Ervindas",
    soldQuantity: Int = 10,
    quantity: Int = 100,
    price: Int = 50,
    isSoldOut: Boolean
) {
    Column(
        modifier = Modifier,
    ) {
        MashiHolder()

        Spacer(modifier = Modifier.height(ExtraSmallPaddingSize))

        Text(text = avatarName, fontSize = 14.sp, color = Color.White)

        Text(text = "by $authorName", fontSize = 12.sp, color = ContentColor)

        Text(text = "$soldQuantity of $quantity sold", fontSize = 12.sp, color = ContentColor)

        BuyButton(
            text = if (isSoldOut) "Sold out" else "$price USDC",
            enabled = !isSoldOut,
        )
    }
}

@Preview
@Composable
private fun ShopItemPreview() {
    ShopItem(i = 1, isSoldOut = true)
}