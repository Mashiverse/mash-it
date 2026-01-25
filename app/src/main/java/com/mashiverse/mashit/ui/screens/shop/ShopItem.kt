package com.mashiverse.mashit.ui.screens.shop

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mashiverse.mashit.data.models.image.ImageType
import com.mashiverse.mashit.data.models.mashi.ListingDetails
import com.mashiverse.mashit.ui.screens.buttons.BuyButton
import com.mashiverse.mashit.ui.screens.mashi.trait.Trait
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.ExtraSmallPaddingSize
import com.mashiverse.mashit.ui.theme.LargeMashiHolderHeight
import com.mashiverse.mashit.ui.theme.LargeMashiHolderWidth
import com.mashiverse.mashit.ui.theme.MashiHolderShape

@Composable
fun ShopItem(
    listingDetails: ListingDetails,
    selectId: (String) -> Unit,
    getImageType: (String) -> ImageType?,
    setImageType: (ImageType, String) -> Unit,
) {
    val productInfo = listingDetails.productInfo
    val isSoldOut = productInfo.soldQuantity >= productInfo.quantity

    Column(
        modifier = Modifier,
    ) {
        Trait(
            modifier = Modifier
                .width(LargeMashiHolderWidth)
                .height(LargeMashiHolderHeight)
                .border(width = 0.3.dp, shape = MashiHolderShape, color = ContentColor),
            onClick = { selectId.invoke(listingDetails.id) },
            data = listingDetails.compositeUrl,
            getImageType =getImageType,
            setImageType = setImageType
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