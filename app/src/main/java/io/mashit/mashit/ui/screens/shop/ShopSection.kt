package io.mashit.mashit.ui.screens.shop

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import io.mashit.mashit.data.models.image.ImageType
import io.mashit.mashit.data.models.mashi.ListingDetails
import io.mashit.mashit.ui.theme.ContentAccentColor
import io.mashit.mashit.ui.theme.ContentColor
import io.mashit.mashit.ui.theme.SmallPaddingSize

@Composable
fun ShopSection(
    sectionName: String,
    selectId: (String) -> Unit,
    sectionItems: List<ListingDetails>,
    getImageType: (String) -> ImageType?,
    setImageType: (ImageType, String) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = sectionName,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = ContentAccentColor
            )

            Spacer(modifier = Modifier.weight(1F))

            TextButton(
                onClick = {/*TODO*/ }) {
                Text(
                    text = "See all",
                    fontSize = 14.sp,
                    color = ContentColor,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(SmallPaddingSize)
        ) {
            items(sectionItems.size) { i ->
                ShopItem(
                    listingDetails = sectionItems[i],
                    selectId = selectId,
                    getImageType = getImageType,
                    setImageType = setImageType
                )
            }
        }
    }
}