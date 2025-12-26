package dev.tymoshenko.mashit.ui.screens.main.shop

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.tymoshenko.mashit.ui.screens.main.mashi.MashiHolder
import dev.tymoshenko.mashit.ui.theme.ContainerColor
import dev.tymoshenko.mashit.ui.theme.ContentColor
import dev.tymoshenko.mashit.ui.theme.ExtraSmallPaddingSize
import dev.tymoshenko.mashit.ui.theme.SmallPaddingSize

@Composable
fun BuyButton(
    text: String,
    enabled: Boolean,
    height: Dp = 28.dp,
    width: Dp = 72.dp,
    textSize: TextUnit = 12.sp
) {
    val gradient = Brush.horizontalGradient(
        colors = listOf(
            Color(165, 9, 9),
            Color(100, 0, 0)
        )
    )

    Button(
        modifier = Modifier
            .height(height)
            .width(width)
            .background(
                brush = if (enabled) {
                    gradient
                } else {
                    Brush.horizontalGradient(listOf(Color.Transparent, Color.Transparent))
                },
                shape = ButtonDefaults.shape
            )
            .clip(shape = ButtonDefaults.shape),
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = Color.Transparent,
            disabledContainerColor = ContainerColor
        ),
        enabled = enabled,
        onClick = {/*TODO*/ },
        border = BorderStroke(
            width = (1).dp,
            color = if(enabled) {
                Color.Red
            } else {
                Color.Transparent
            }
        ),

        contentPadding = PaddingValues(horizontal = SmallPaddingSize),
    ) {
        Text(
            text = text,
            fontSize = textSize,
            color = if (enabled) {
                Color.White
            } else {
                ContentColor
            },
            fontWeight = if (enabled) {
                FontWeight.SemiBold
            } else {
                FontWeight.Normal
            },
        )
    }
}

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
    ShopItem(i=1, isSoldOut = true)
}