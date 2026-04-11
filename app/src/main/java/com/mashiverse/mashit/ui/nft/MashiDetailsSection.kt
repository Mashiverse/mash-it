package com.mashiverse.mashit.ui.nft

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coinbase.android.nativesdk.CoinbaseWalletSDK
import com.mashiverse.mashit.data.intents.Web3Intent
import com.mashiverse.mashit.data.models.nft.Nft
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.Padding
import com.mashiverse.mashit.ui.theme.ShopHolderHeight
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
    processWeb3Intent: ((Web3Intent) -> Unit)? = null,
    clientRef: CoinbaseWalletSDK? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(ShopHolderHeight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.weight(1f))

                IconButton(
                    modifier = Modifier.size(32.dp),
                    onClick = {}
                ) {
                    Icon(
                        modifier = Modifier
                            .size(32.dp),
                        imageVector = Icons.Default.MoreHoriz,
                        contentDescription = "More",
                        tint = ContentAccentColor
                    )
                }

                Spacer(modifier = Modifier.width(SmallPadding))

                IconButton(
                    modifier = Modifier.size(32.dp),
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
                            .size(32.dp),
                        imageVector = Icons.Default.Clear,
                        contentDescription = "More",
                        tint = ContentAccentColor
                    )
                }
            }

            Spacer(Modifier.height(Padding))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = nft.name,
                    maxLines = 1,
                    color = ContentAccentColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "by ${nft.author}",
                    color = ContentColor,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(Padding))

            nft.description?.let {
                Text(
                    maxLines = 5,
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
                    processWeb3Intent = processWeb3Intent!!,
                    clientRef = clientRef!!
                )
            }
        }
    }
}