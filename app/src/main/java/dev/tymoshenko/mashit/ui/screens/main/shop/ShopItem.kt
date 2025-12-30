package dev.tymoshenko.mashit.ui.screens.main.shop

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import dev.tymoshenko.mashit.data.models.mashi.ListingDetails
import dev.tymoshenko.mashit.data.models.mashi.MashiDetails
import dev.tymoshenko.mashit.data.models.mashi.ProductInfo
import dev.tymoshenko.mashit.ui.screens.main.buttons.BuyButton
import dev.tymoshenko.mashit.ui.screens.main.mashi.trait.Trait
import dev.tymoshenko.mashit.ui.theme.ContentAccentColor
import dev.tymoshenko.mashit.ui.theme.ContentColor
import dev.tymoshenko.mashit.ui.theme.ExtraSmallPaddingSize
import dev.tymoshenko.mashit.ui.theme.LargeMashiHolderHeight
import dev.tymoshenko.mashit.ui.theme.LargeMashiHolderWidth
import dev.tymoshenko.mashit.ui.theme.MashiHolderHeight
import dev.tymoshenko.mashit.ui.theme.MashiHolderWidth

@Composable
fun ShopItem(
    listingDetails: ListingDetails,
    selectId: (String) -> Unit,
) {
    val productInfo = listingDetails.productInfo
    val isSoldOut = productInfo.soldQuantity >= productInfo.quantity

    Column(
        modifier = Modifier,
    ) {
        Trait(
            modifier = Modifier
                .width(LargeMashiHolderWidth)
                .height(LargeMashiHolderHeight),
            onClick = { selectId.invoke(listingDetails.id) },
            data = listingDetails.compositeUrl
        )

        Spacer(modifier = Modifier.height(ExtraSmallPaddingSize))

        Text(text = listingDetails.name, fontSize = 14.sp, color = ContentAccentColor)

        Spacer(modifier = Modifier.height(ExtraSmallPaddingSize))

        Text(text = "by ${listingDetails.author}", fontSize = 12.sp, color = ContentColor)

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