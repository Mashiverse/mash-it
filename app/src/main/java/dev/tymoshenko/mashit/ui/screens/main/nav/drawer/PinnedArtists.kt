package dev.tymoshenko.mashit.ui.screens.main.nav.drawer

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
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
import dev.tymoshenko.mashit.ui.theme.ContentColor
import dev.tymoshenko.mashit.ui.theme.ContentContainerHeight
import dev.tymoshenko.mashit.ui.theme.ContentTextSize
import dev.tymoshenko.mashit.ui.theme.IconSize
import dev.tymoshenko.mashit.ui.theme.IconsShape
import dev.tymoshenko.mashit.ui.theme.LargePaddingSize
import dev.tymoshenko.mashit.ui.theme.PaddingSize
import dev.tymoshenko.mashit.ui.theme.SmallIconSize
import dev.tymoshenko.mashit.ui.theme.SmallPaddingSize

@Composable
private fun PinnedArtist() {
    Row(
        modifier = Modifier
            .height(ContentContainerHeight)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(PaddingSize))

        Box(
            modifier = Modifier
                .size(SmallIconSize)
                .clip(IconsShape)
                .background(Color.White)
        )

        Spacer(modifier = Modifier.width(SmallPaddingSize))

        Text(
            "Ervindas rocks!",
            fontSize = ContentTextSize,
            color = ContentColor,
            fontWeight = FontWeight.Normal
        )

        Spacer(modifier = Modifier.weight(1F))

        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                modifier = Modifier
                    .size(SmallIconSize),
                imageVector = Icons.Default.PushPin,
                contentDescription = "unpin",
                tint = ContentColor
            )
        }

        Spacer(modifier = Modifier.width(LargePaddingSize))
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
            Spacer(modifier = Modifier.width(PaddingSize))

            Text(
                "Pinned Artists",
                fontSize = ContentTextSize,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1F))

            IconButton(onClick = { isOpened = !isOpened }) {
                Icon(
                    modifier = Modifier
                        .size(IconSize),
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
                verticalArrangement = Arrangement.spacedBy(PaddingSize)
            ) {
                repeat(6) {
                    PinnedArtist()
                }
            }
        }
    }
}