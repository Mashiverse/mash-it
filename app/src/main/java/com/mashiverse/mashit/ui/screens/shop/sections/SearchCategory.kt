package com.mashiverse.mashit.ui.screens.shop.sections

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.mashiverse.mashit.data.models.image.ImageType
import com.mashiverse.mashit.data.models.nft.Nft
import com.mashiverse.mashit.ui.screens.shop.items.ShopItem
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.PaddingSize
import com.mashiverse.mashit.ui.theme.SmallPaddingSize

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun SearchCategory(
    items: List<Nft>,
    selectId: (String) -> Unit,
    getImageType: (String) -> ImageType?,
    setImageType: (ImageType, String) -> Unit,
    getSoldQty: (Int, (Int) -> Unit) -> Unit,
    onMint: (String, Double) -> Unit,
    onSearchClear: () -> Unit
) {
    val config = LocalConfiguration.current
    val imageWidth = (config.screenWidthDp.dp - 2 * PaddingSize - SmallPaddingSize) / 2
    val imageHeight = (imageWidth / 3) * 4

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Search",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = ContentAccentColor
            )

            Spacer(modifier = Modifier.weight(1F))

            TextButton(
                onClick = {
                    onSearchClear.invoke()
                }
            ) {
                Text(
                    text = "Clear",
                    fontSize = 14.sp,
                    color = ContentColor,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // Sets 2 items per row
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(SmallPaddingSize),
            verticalArrangement = Arrangement.spacedBy(PaddingSize)
        ) {
            items(
                items.size
            ) { index ->
                val nft = items[index]
                ShopItem(
                    nft = nft,
                    selectId = selectId,
                    getImageType = getImageType,
                    setImageType = setImageType,
                    getSoldQty = getSoldQty,
                    onMint = onMint,
                    imageWidth = imageWidth,
                    imageHeight = imageHeight
                )
            }
        }
    }
}