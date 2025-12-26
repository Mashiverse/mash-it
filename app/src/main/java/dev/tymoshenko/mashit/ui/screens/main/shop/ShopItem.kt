package dev.tymoshenko.mashit.ui.screens.main.shop

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import dev.tymoshenko.mashit.data.models.mashi.MashiDetails
import dev.tymoshenko.mashit.data.models.mashi.ervindasExample
import dev.tymoshenko.mashit.ui.screens.main.buttons.BuyButton
import dev.tymoshenko.mashit.ui.screens.main.mashi.TraitHolder
import dev.tymoshenko.mashit.ui.theme.ContentAccentColor
import dev.tymoshenko.mashit.ui.theme.ContentColor
import dev.tymoshenko.mashit.ui.theme.ExtraSmallPaddingSize

@Composable
fun ShopItem(
    mashiDetails: MashiDetails,
    selectMashi: (MashiDetails) -> Unit,
) {
    val isSoldOut = mashiDetails.soldQuantity >= mashiDetails.quantity

    Column(
        modifier = Modifier,
    ) {
        TraitHolder(
            onClick = { selectMashi.invoke(mashiDetails) },
            data = mashiDetails.compositeUrl
        )

        Spacer(modifier = Modifier.height(ExtraSmallPaddingSize))

        Text(text = mashiDetails.name, fontSize = 14.sp, color = ContentAccentColor)

        Text(text = "by ${mashiDetails.author}", fontSize = 12.sp, color = ContentColor)

        Text(text = "${mashiDetails.soldQuantity} of ${mashiDetails.quantity} sold", fontSize = 12.sp, color = ContentColor)

        BuyButton(
            text = if (isSoldOut) "Sold out" else "${mashiDetails.price} ${mashiDetails.priceCurrency.name}",
            enabled = !isSoldOut,
        )
    }
}

@Preview
@Composable
private fun ShopItemPreview() {
    ShopItem(mashiDetails = ervindasExample, selectMashi = {})
}