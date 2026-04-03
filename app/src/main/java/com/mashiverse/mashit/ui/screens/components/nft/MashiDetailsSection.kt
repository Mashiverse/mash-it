package com.mashiverse.mashit.ui.screens.components.nft

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.mashiverse.mashit.R
import com.mashiverse.mashit.data.models.nft.Nft
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.ExtraSmallPadding
import com.mashiverse.mashit.ui.theme.LargeMashiHolderHeight
import com.mashiverse.mashit.ui.theme.SmallIconSize
import com.mashiverse.mashit.ui.theme.SmallPadding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MashiDetailsSection(
    nft: Nft,
    scope: CoroutineScope,
    closeBottomSheet: () -> Unit,
    sheetState: SheetState,
    getSoldQty: ((Int, (Int) -> Unit) -> Unit)? = null,
    onMint: ((String, Double, Boolean) -> Unit)? = null
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
                    text = if (nft.name.length > 20) {
                        "${nft.name.take(17)}..."
                    } else {
                        nft.name
                    },
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

                Spacer(modifier = Modifier.width(SmallPadding))

                IconButton(
                    modifier = Modifier.size(SmallIconSize),
                    onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                closeBottomSheet.invoke()
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

            Text(text = "by ${nft.author}", color = ContentColor, fontSize = 12.sp)

            Spacer(modifier = Modifier.height(ExtraSmallPadding))

            nft.description?.let {
                Text(
                    maxLines = 6,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 12.sp,
                    color = ContentAccentColor,
                    text = it,
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            if (nft.productInfo != null) {
                ProductInfoSection(
                    nft = nft,
                    getSoldQty = getSoldQty,
                    onMint = onMint
                )
            }
        }
    }
}