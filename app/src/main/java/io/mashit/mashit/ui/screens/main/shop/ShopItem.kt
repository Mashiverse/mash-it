package io.mashit.mashit.ui.screens.main.shop

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import io.mashit.mashit.data.models.mashi.ListingDetails
import io.mashit.mashit.ui.screens.main.buttons.BuyButton
import io.mashit.mashit.ui.screens.main.mashi.trait.Trait
import io.mashit.mashit.ui.theme.ContentAccentColor
import io.mashit.mashit.ui.theme.ContentColor
import io.mashit.mashit.ui.theme.ExtraSmallPaddingSize
import io.mashit.mashit.ui.theme.LargeMashiHolderHeight
import io.mashit.mashit.ui.theme.LargeMashiHolderWidth

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