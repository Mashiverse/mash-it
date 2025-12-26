package dev.tymoshenko.mashit.ui.screens.main.nav.top

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import dev.tymoshenko.mashit.R

import dev.tymoshenko.mashit.ui.theme.ContentAccentColor
import dev.tymoshenko.mashit.ui.theme.ContentTextSize
import dev.tymoshenko.mashit.ui.theme.SearchHeight
import dev.tymoshenko.mashit.ui.theme.SearchShape
import dev.tymoshenko.mashit.ui.theme.SmallIconSize

@Composable
fun SearchBar(
    isSearch: Boolean,
    onIsSearchChange: () -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    val width = animateDpAsState(
        targetValue = if (isSearch) {
            272.dp
        } else {
            SearchHeight
        }
    )

    val borderColor = animateColorAsState(
        targetValue = if (isSearch) {
            Color.Red
        } else {
            Color.Transparent
        },
        animationSpec = if (isSearch) {
            tween(durationMillis = 300)
        } else {
            tween(durationMillis = 250)
        }
    )

    val iconOffset = animateDpAsState(
        targetValue = if (isSearch) {
            0.dp
        } else {
            (-12).dp
        },
    )

    Row(
        modifier = Modifier
            .height(SearchHeight)
            .width(width.value)
            .clip(shape = SearchShape)
            .border(
                border = BorderStroke(width = 1.dp, color = borderColor.value),
                shape = SearchShape
            )
            .wrapContentSize(unbounded = true, align = Alignment.CenterStart)
            .clipToBounds(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        if (width.value <= SearchHeight) {
            Icon(
                modifier = Modifier
                    .size(SmallIconSize)
                    .clickable { onIsSearchChange.invoke() },
                painter = painterResource(R.drawable.search_icon),
                contentDescription = "Search icon",
                tint = ContentAccentColor
            )
        } else {
            TextField(
                modifier = Modifier
                    .width(width.value)
                    .wrapContentSize(unbounded = true, align = Alignment.CenterStart)
                    .clipToBounds(),
                value = searchQuery,
                onValueChange = { input -> onSearchQueryChange.invoke(input) },
                leadingIcon = {
                    Icon(
                        modifier = Modifier
                            .size(SmallIconSize)
                            .offset(x = iconOffset.value)
                            .clickable {
                                onIsSearchChange.invoke()
                                onSearchQueryChange.invoke("")
                            },
                        painter = painterResource(R.drawable.search_icon),
                        contentDescription = "Search icon",
                        tint = ContentAccentColor
                    )
                },
                colors = TextFieldDefaults.colors().copy(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent
                ),
                singleLine = true,
                textStyle = TextStyle(fontSize = ContentTextSize),
                placeholder = { Text(text = "Search Mash It", fontSize = ContentTextSize) }
            )
        }
    }
}