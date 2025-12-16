package dev.tymoshenko.mashit.ui.screens.main.components.nav

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.tymoshenko.mashit.R
import dev.tymoshenko.mashit.ui.theme.SearchBarBackground
import kotlinx.coroutines.delay

@Composable
fun SearchBar(
    isSearch: Boolean,
    onIsSearchChange: () -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    var isIcon by remember {
        mutableStateOf(true)
    }

    val width = animateDpAsState(
        targetValue = if (isSearch) {
            256.dp
        } else {
            32.dp
        },
        animationSpec = tween(delayMillis = 300)
    )

    val background = animateColorAsState(
        targetValue = if (isSearch) {
            SearchBarBackground
        } else {
            Color.Transparent
        },
        animationSpec = tween(delayMillis = 300)
    )

    val startPadding = animateDpAsState(
        targetValue = if (isSearch) {
            8.dp
        } else {
            0.dp
        },
        animationSpec = tween(delayMillis = 300)
    )

    LaunchedEffect(isSearch) {
        if (!isSearch) {
            delay(300)
            isIcon = true
        } else {
            delay(600)
            isIcon = false
        }
    }

    Row(
        modifier = Modifier
            .height(32.dp)
            .width(width.value)
            .clip(shape = RoundedCornerShape(24.dp))
            .background(background.value)
            .wrapContentSize(unbounded = true, align = Alignment.CenterStart)
            .clipToBounds(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Spacer(modifier = Modifier.width(startPadding.value))

        if (isIcon) {
            Spacer(modifier = Modifier.width(startPadding.value + startPadding.value / 2 ))
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onIsSearchChange.invoke() },
                painter = painterResource(R.drawable.search_icon),
                contentDescription = "Search icon",
                tint = Color.White
            )
        } else {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = searchQuery,
                onValueChange = { input -> onSearchQueryChange.invoke(input) },
                leadingIcon = {
                    Icon(
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                onIsSearchChange.invoke()
                                onSearchQueryChange.invoke("")
                            },
                        painter = painterResource(R.drawable.search_icon),
                        contentDescription = "Search icon",
                        tint = Color.White
                    )
                },
                colors = TextFieldDefaults.colors().copy(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent
                ),
                singleLine = true,
                textStyle = TextStyle(fontSize = 14.sp),
                placeholder = { Text("Search Mash It", fontSize = 14.sp) }
            )
        }

        Spacer(modifier = Modifier.width(8.dp))
    }
}