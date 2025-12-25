package dev.tymoshenko.mashit.ui.screens.main.components.nav.drawer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.tymoshenko.mashit.ui.theme.ContentColor

@Composable
private fun PinnedArtist() {
    Row(
        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(16.dp))

        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(90))
                .background(Color.White)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            "Ervindas rocks!",
            fontSize = 16.sp,
            color = ContentColor,
            fontWeight = FontWeight.Normal
        )

        Spacer(modifier = Modifier.weight(1F))

        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                modifier = Modifier
                    .size(24.dp),
                imageVector = Icons.Default.PushPin,
                contentDescription = "unpin",
                tint = ContentColor
            )
        }

        Spacer(modifier = Modifier.width(32.dp))
    }
}

@Composable
fun PinnedArtists(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        var isOpened by remember {
            mutableStateOf(false)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(16.dp))

            Text(
                "Pinned Artists",
                fontSize = 16.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1F))

            IconButton(onClick = { isOpened = !isOpened }) {
                Icon(
                    modifier = Modifier
                        .size(32.dp),
                    imageVector = if (!isOpened) {
                        Icons.Default.ArrowDropDown
                    } else {
                        Icons.Default.ArrowDropUp
                    },
                    contentDescription = "drop list",
                    tint = Color.White
                )
            }
        }

        AnimatedVisibility(
            visible = isOpened,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                repeat(6) {
                    PinnedArtist()
                }
            }
        }
    }
}