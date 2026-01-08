package io.mashit.mashit.ui.screens.main.mashi

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.mashit.mashit.data.models.mashi.ProductInfo
import io.mashit.mashit.ui.screens.main.buttons.BuyButton
import io.mashit.mashit.ui.theme.ContentAccentColor
import io.mashit.mashit.ui.theme.ContentColor
import io.mashit.mashit.ui.theme.ExtraSmallPaddingSize
import io.mashit.mashit.ui.theme.PaddingSize

@Composable
fun MashiProductInfoSection(
    productInfo: ProductInfo
) {
    Text(
        text = "${productInfo.price} ${productInfo.priceCurrency.name}",
        color = ContentAccentColor,
        fontSize = 14.sp
    )


    Spacer(modifier = Modifier.height(PaddingSize))

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Per-wallet: ${productInfo.perWallet}",
                color = ContentColor,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(ExtraSmallPaddingSize))

            Text(
                text = "${productInfo.soldQuantity}/${productInfo.quantity} sold",
                color = ContentColor,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.weight(1F))

        BuyButton(
            text = if (productInfo.soldQuantity < productInfo.quantity) "Buy" else "Sold",
            height = 32.dp,
            width = 80.dp,
            enabled = productInfo.soldQuantity < productInfo.quantity,
            textSize = 16.sp
        )
    }
}