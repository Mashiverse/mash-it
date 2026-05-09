package com.mashiverse.mashit.ui.screens.shop.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mashiverse.mashit.ui.theme.Padding
import com.mashiverse.mashit.ui.theme.TraitShape

@Composable
fun Drops() {
    Column {
        LazyRow {
            item {
                Box(
                    modifier = Modifier
                        .width((44.1 * 2).dp)
                        .height((63.6 * 2).dp)
                        .clip(TraitShape)
                        .background(Color.Red)
                )
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