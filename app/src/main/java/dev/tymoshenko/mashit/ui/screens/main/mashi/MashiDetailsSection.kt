package dev.tymoshenko.mashit.ui.screens.main.mashi

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
import dev.tymoshenko.mashit.R
import dev.tymoshenko.mashit.data.models.mashi.MashiDetails
import dev.tymoshenko.mashit.ui.theme.ContentAccentColor
import dev.tymoshenko.mashit.ui.theme.ContentColor
import dev.tymoshenko.mashit.ui.theme.ExtraSmallPaddingSize
import dev.tymoshenko.mashit.ui.theme.LargeMashiHolderHeight
import dev.tymoshenko.mashit.ui.theme.SmallIconSize
import dev.tymoshenko.mashit.ui.theme.SmallPaddingSize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MashiDetailsSection(
    mashiDetails: MashiDetails,
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

            if (mashiDetails.productInfo != null) {
                MashiProductInfoSection(mashiDetails.productInfo)
            }
        }
    }
}