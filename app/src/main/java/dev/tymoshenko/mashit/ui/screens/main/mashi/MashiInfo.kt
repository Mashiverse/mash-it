package dev.tymoshenko.mashit.ui.screens.main.mashi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Money
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.tymoshenko.mashit.ui.screens.main.shop.BuyButton
import dev.tymoshenko.mashit.ui.theme.Background
import dev.tymoshenko.mashit.ui.theme.ContentColor
import dev.tymoshenko.mashit.ui.theme.LargeMashiHolderHeight
import dev.tymoshenko.mashit.ui.theme.LargeMashiHolderWidth
import dev.tymoshenko.mashit.ui.theme.PaddingSize
import dev.tymoshenko.mashit.ui.theme.SmallIconSize
import dev.tymoshenko.mashit.ui.theme.SmallPaddingSize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MashiInfo(scope: CoroutineScope, closeBottomShit: () -> Unit, sheetState: SheetState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(LargeMashiHolderHeight),
        horizontalArrangement = Arrangement.spacedBy(PaddingSize)
    ) {
        MashiHolder(
            width = LargeMashiHolderWidth,
            height = LargeMashiHolderHeight
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(text = "Avatar Name", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.weight(1F))

                IconButton(
                    modifier = Modifier.size(SmallIconSize),
                    onClick = {}
                ) {
                    Icon(
                        modifier = Modifier
                            .size(SmallIconSize),
                        imageVector = Icons.Default.Money,
                        contentDescription = "More",
                        tint = Color.White
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
                        imageVector = Icons.Default.Close,
                        contentDescription = "More",
                        tint = Color.White
                    )
                }
            }

            Text(text = "by Ervindas", color = ContentColor, fontSize = 12.sp)


            Text(
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                fontSize = 12.sp,
                color = Color.White,
                text = "fgsdgssdgsegwetwetwetwetwfgsdgssdgsegwetwetwetwetwfgsdgssdgsegwetwetwetwetwfgsdgssdgsegwetwetwetwetwfgsdgssdgsegwetwetwetwetwfgsdgssdgsegwetwetwetwetwfgsdgssdgsegwetwetwetwetwfgsdgssdgsegwetwetwetwetwfgsdgssdgsegwetwetwetwetw")

            Spacer(modifier = Modifier.weight(1f))

            Text(text = "50 USDC", color = Color.White, fontSize = 14.sp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Max per wallet: 50", color = ContentColor, fontSize = 12.sp)
                    Text(text = "74 of 100 sold", color = ContentColor, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.weight(1F))

                BuyButton(
                    text = "Buy",
                    height = 32.dp,
                    enabled = true,
                    textSize = 16.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MashiInfoPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
//        MashiInfo()
    }
}