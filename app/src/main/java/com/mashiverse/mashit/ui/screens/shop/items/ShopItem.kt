package com.mashiverse.mashit.ui.screens.shop.items

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mashiverse.mashit.data.models.image.ImageType
import com.mashiverse.mashit.data.models.nft.Nft
import com.mashiverse.mashit.data.models.nft.PriceCurrency
import com.mashiverse.mashit.ui.screens.components.buttons.BuyButton
import com.mashiverse.mashit.ui.screens.components.nft.trait.TraitImage
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
    nft: Nft,
    selectId: (String) -> Unit,
    getImageType: (String) -> ImageType?,
    setImageType: (ImageType, String) -> Unit,
    getSoldQty: (Int, (Int) -> Unit) -> Unit,
    onMint: (String, Double, Boolean) -> Unit,
    imageWidth: Dp = LargeMashiHolderWidth,
    imageHeight: Dp = LargeMashiHolderHeight
) {
    val scope = rememberCoroutineScope()
    val productInfo = nft.productInfo

    var soldQty by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(Unit) {
        scope.launch(Dispatchers.IO) {
            getSoldQty.invoke(productInfo?.listingId?.toInt() ?: -1) { v ->
                soldQty = v
            }
        }
    }

    val delisted = nft.productInfo?.delisted ?: false

    val isSoldOut by remember(soldQty) {
        mutableStateOf(soldQty >= (productInfo?.quantity ?: -1))
    }

    Column(
        modifier = Modifier
            .width(imageWidth),
    ) {
        TraitImage(
            modifier = Modifier
                .width(imageWidth)
                .height(imageHeight)
                .border(width = 0.3.dp, shape = MashiHolderShape, color = ContentColor),
            onClick = { selectId.invoke(productInfo?.id ?: "") },
            data = nft.compositeUrl,
            getImageType = getImageType,
            setImageType = setImageType
        )

        Spacer(modifier = Modifier.height(ExtraSmallPaddingSize))

        Text(
            text = nft.name,
            overflow = TextOverflow.Ellipsis, maxLines = 1, fontSize = 14.sp, color = ContentAccentColor
        )

        Spacer(modifier = Modifier.height(ExtraSmallPaddingSize))

        Text(text = "by ${nft.author}", fontSize = 12.sp, color = ContentColor)

        Spacer(modifier = Modifier.height(ExtraSmallPaddingSize))

        Text(
            text = "$soldQty of ${productInfo?.quantity ?: -1} sold",
            fontSize = 12.sp,
            color = ContentColor
        )

        Spacer(modifier = Modifier.height(ExtraSmallPaddingSize))

        BuyButton(
            text = when {
                isSoldOut -> "Sold out"
                delisted -> "Delisted"
                else -> "${productInfo?.price?.toInt()} ${productInfo?.priceCurrency?.name}"
            },
            enabled = !isSoldOut && !delisted,
            onClick = {
                if (nft.productInfo?.listingId != null) {
                    onMint.invoke(nft.productInfo.listingId, nft.productInfo.price, nft.productInfo.priceCurrency == PriceCurrency.POL)
                }
            }
        )
    }
}