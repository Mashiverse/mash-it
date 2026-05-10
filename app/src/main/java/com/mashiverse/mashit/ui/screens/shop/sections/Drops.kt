package com.mashiverse.mashit.ui.screens.shop.sections

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.mashiverse.mashit.data.models.drops.DropDetails
import com.mashiverse.mashit.ui.default.indicators.LoadingIndicator
import com.mashiverse.mashit.ui.default.indicators.SectionLoading
import com.mashiverse.mashit.ui.theme.Padding
import com.mashiverse.mashit.ui.theme.SmallPadding
import com.mashiverse.mashit.ui.theme.TraitShape

@Composable
fun Drops(specialDrops: List<DropDetails>) {
    val context = LocalContext.current

    Column {
        LazyRow(
            modifier = Modifier.fillMaxWidth()
                .height((63.6 * 2).dp),
            horizontalArrangement = Arrangement.spacedBy(SmallPadding)
        ) {
            if (specialDrops.isEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillParentMaxSize(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        LoadingIndicator()
                    }
                }
            }

            items(
                specialDrops.size,
            ) { i ->
                val drop = specialDrops[i]

                DropImage(
                    slug = drop.slug,
                    imageUrl = drop.imageUrl
                ) {
                    Toast.makeText(context, drop.slug, Toast.LENGTH_LONG).show()
                }
            }
        }

        Spacer(modifier = Modifier.height(Padding))

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.DarkGray)
        )

        Spacer(modifier = Modifier.height(Padding))
    }
}