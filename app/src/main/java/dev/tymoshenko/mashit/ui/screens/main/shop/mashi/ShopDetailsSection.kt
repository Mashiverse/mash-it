package dev.tymoshenko.mashit.ui.screens.main.shop.mashi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.tymoshenko.mashit.R
import dev.tymoshenko.mashit.data.models.mashi.MashiDetails
import dev.tymoshenko.mashit.data.models.mashi.ervindasExample
import dev.tymoshenko.mashit.ui.screens.main.buttons.BuyButton
import dev.tymoshenko.mashit.ui.theme.Background
import dev.tymoshenko.mashit.ui.theme.ContentAccentColor
import dev.tymoshenko.mashit.ui.theme.ContentColor
import dev.tymoshenko.mashit.ui.theme.ExtraSmallPaddingSize
import dev.tymoshenko.mashit.ui.theme.LargeMashiHolderHeight
import dev.tymoshenko.mashit.ui.theme.PaddingSize
import dev.tymoshenko.mashit.ui.theme.SmallIconSize
import dev.tymoshenko.mashit.ui.theme.SmallPaddingSize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopDetailsSection(
    mashiDetails: MashiDetails = ervindasExample,
    scope: CoroutineScope,
    closeBottomShit: () -> Unit,
    sheetState: SheetState
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(LargeMashiHolderHeight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = mashiDetails.name,
                    color = ContentAccentColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.weight(1F))

                IconButton(
                    modifier = Modifier.size(SmallIconSize),
                    onClick = {}
                ) {
                    Icon(
                        modifier = Modifier
                            .size(SmallIconSize),
                        painter = painterResource(R.drawable.expand_icon),
                        contentDescription = "More",
                        tint = ContentAccentColor
                    )
                }

                Spacer(modifier = Modifier.width(SmallPaddingSize))

                IconButton(
                    modifier = Modifier.size(SmallIconSize),
                    onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                closeBottomShit.invoke()
                            }
                        }
                    }
                ) {
                    Icon(
                        modifier = Modifier
                            .size(SmallIconSize),
                        painter = painterResource(R.drawable.close_icon),
                        contentDescription = "More",
                        tint = ContentAccentColor
                    )
                }
            }

            Text(text = "by ${mashiDetails.author}", color = ContentColor, fontSize = 12.sp)

            Spacer(modifier = Modifier.height(ExtraSmallPaddingSize))

            Text(
                maxLines = 6,
                overflow = TextOverflow.Ellipsis,
                fontSize = 12.sp,
                color = ContentAccentColor,
                text = mashiDetails.description,
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "${mashiDetails.price} ${mashiDetails.priceCurrency.name}",
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
                        text = "Per-wallet: ${mashiDetails.perWallet}",
                        color = ContentColor,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(ExtraSmallPaddingSize))

                    Text(
                        text = "${mashiDetails.soldQuantity}/${mashiDetails.quantity} sold",
                        color = ContentColor,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.weight(1F))

                BuyButton(
                    text = if (mashiDetails.soldQuantity < mashiDetails.quantity) "Buy" else "Sold",
                    height = 32.dp,
                    width = 80.dp,
                    enabled = mashiDetails.soldQuantity < mashiDetails.quantity,
                    textSize = 16.sp
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun ShopDetailsSectionPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        ShopDetailsSection(
            scope = rememberCoroutineScope(),
            closeBottomShit = {},
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        )
    }
}