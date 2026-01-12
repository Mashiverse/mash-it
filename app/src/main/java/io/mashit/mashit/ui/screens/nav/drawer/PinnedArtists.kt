package io.mashit.mashit.ui.screens.nav.drawer

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import io.mashit.mashit.R

import io.mashit.mashit.ui.theme.ContentAccentColor
import io.mashit.mashit.ui.theme.ContentColor
import io.mashit.mashit.ui.theme.ContentContainerHeight
import io.mashit.mashit.ui.theme.ContentTextSize
import io.mashit.mashit.ui.theme.IconsShape
import io.mashit.mashit.ui.theme.LargePaddingSize
import io.mashit.mashit.ui.theme.PaddingSize
import io.mashit.mashit.ui.theme.SmallIconSize
import io.mashit.mashit.ui.theme.SmallPaddingSize

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
                .background(ContentAccentColor)
        )

        Spacer(modifier = Modifier.width(SmallPaddingSize))

        Text(
            text = "Ervindas rocks!",
            fontSize = ContentTextSize,
            color = ContentColor,
            fontWeight = FontWeight.Normal
        )

        Spacer(modifier = Modifier.weight(1F))

        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                modifier = Modifier
                    .size(SmallIconSize),
                painter = painterResource(R.drawable.pin_icon),
                contentDescription = "pin",
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
                text = "Pinned Artists",
                fontSize = ContentTextSize,
                color = ContentAccentColor,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1F))

            IconButton(
                modifier = Modifier
                    .size(SmallIconSize),
                onClick = { isOpened = !isOpened }
            ) {
                Icon(
                    modifier = Modifier
                        .size(SmallIconSize),
                    painter = if (!isOpened) {
                        painterResource(R.drawable.drop_down_icon)
                    } else {
                        painterResource(R.drawable.drop_up_icon)
                    },
                    contentDescription = "drop list",
                    tint = ContentAccentColor
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