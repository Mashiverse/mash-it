package com.mashiverse.mashit.ui.screens.shop

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mashiverse.mashit.data.models.image.ImageType
import com.mashiverse.mashit.data.models.mashi.NftDetails
import com.mashiverse.mashit.ui.screens.buttons.BuyButton
import com.mashiverse.mashit.ui.screens.mashi.trait.Trait
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.ExtraSmallPaddingSize
import com.mashiverse.mashit.ui.theme.LargeMashiHolderHeight
import com.mashiverse.mashit.ui.theme.LargeMashiHolderWidth
import com.mashiverse.mashit.ui.theme.MashiHolderShape
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ShopItem(
    listingDetails: NftDetails.ListingDetails,
    selectId: (String) -> Unit,
    getImageType: (String) -> ImageType?,
    setImageType: (ImageType, String) -> Unit,
    getSoldQty: suspend (Int) -> Int
) {
    val scope = rememberCoroutineScope()
    val productInfo = listingDetails.productInfo
    val listingId = listingDetails.listingId

    var soldQty by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(Unit) {
        scope.launch(Dispatchers.IO) {
            soldQty = getSoldQty(listingId.toInt())
        }
    }


    val isSoldOut by remember(soldQty) {
        mutableStateOf(soldQty >= productInfo.quantity)
    }

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
            getImageType = getImageType,
            setImageType = setImageType
        )

        Spacer(modifier = Modifier.height(ExtraSmallPaddingSize))

        Text(text = listingDetails.name, fontSize = 14.sp, color = ContentAccentColor)

        Spacer(modifier = Modifier.height(ExtraSmallPaddingSize))

        Text(text = "by ${listingDetails.author}", fontSize = 12.sp, color = ContentColor)

        Spacer(modifier = Modifier.height(ExtraSmallPaddingSize))

        Text(
            text = "${soldQty} of ${productInfo.quantity} sold",
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