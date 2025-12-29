package dev.tymoshenko.mashit.ui.screens.main.shop

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import dev.tymoshenko.mashit.data.models.mashi.MashiDetails
import dev.tymoshenko.mashit.data.models.mashi.ProductInfo
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
    val productInfo = mashiDetails.productInfo ?: ProductInfo(
        price = 0,
        perWallet = 0,
        soldQuantity = 0,
        quantity = 0
    )
    val isSoldOut = productInfo.soldQuantity >= productInfo.quantity

    Column(
        modifier = Modifier,
    ) {
        TraitHolder(
            onClick = { selectMashi.invoke(mashiDetails) },
            data = mashiDetails.compositeUrl
        )

        Spacer(modifier = Modifier.height(ExtraSmallPaddingSize))

        Text(text = mashiDetails.name, fontSize = 14.sp, color = ContentAccentColor)

        Spacer(modifier = Modifier.height(ExtraSmallPaddingSize))

        Text(text = "by ${mashiDetails.author}", fontSize = 12.sp, color = ContentColor)

        Spacer(modifier = Modifier.height(ExtraSmallPaddingSize))

        Text(
            text = "${productInfo.soldQuantity} of ${productInfo.quantity} sold",
            fontSize = 12.sp,
            color = ContentColor
        )

        Spacer(modifier = Modifier.height(ExtraSmallPaddingSize))

        BuyButton(
            text = if (isSoldOut) "Sold out" else "${productInfo.price} ${productInfo.priceCurrency.name}",
            enabled = !isSoldOut,
        )
    }
}