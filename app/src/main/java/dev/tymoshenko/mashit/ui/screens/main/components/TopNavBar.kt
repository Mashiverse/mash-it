package dev.tymoshenko.mashit.ui.screens.main.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.tymoshenko.mashit.R
import dev.tymoshenko.mashit.ui.theme.MashItTheme
import kotlinx.coroutines.delay

@Composable
fun TopNavBar() {
    var searchQuery by remember { mutableStateOf("") }

    var isSearch by remember {
        mutableStateOf(false)
    }

    var isIcon by remember {
        mutableStateOf(true)
    }

    val endSpacerWeight = animateFloatAsState(
        targetValue = if (!isSearch) {
            0F
        } else {
            1F
        },
        tween(delayMillis = 300)
    )

    LaunchedEffect(isSearch) {
        if (!isSearch) {
            delay(300)
            isIcon = true
        } else {
            delay(300)
            isIcon = false
        }
    }

    val color = animateColorAsState(
        targetValue = if (isSearch) {
            Color(63, 63, 63)
        } else {
            Color.Transparent
        },
        tween(delayMillis = 300)
    )

    val searchWidth = animateDpAsState(
        targetValue = if (isSearch) {
            256.dp
        } else {
            32.dp
        },
        tween(delayMillis = 300)
    )

    val searchPadding = animateDpAsState(
        targetValue = if (isSearch) {
            8.dp
        } else {
            0.dp
        },
        tween(delayMillis = 300)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Row {
                Spacer(Modifier.weight(1F))

                AnimatedVisibility(
                    visible = !isSearch,
                    enter = slideInHorizontally() + expandHorizontally(
                        expandFrom = Alignment.Start,
                        animationSpec = tween(delayMillis = 300)
                    ),
                    exit = fadeOut() //slideOutHorizontally(targetOffsetX = { it -> -it* 2 }),
                ) {
                    Row {
                        OutlinedButton(
                            modifier = Modifier
                                .height(32.dp),
                            colors = ButtonDefaults.outlinedButtonColors().copy(
                                containerColor = Color.Transparent,
                                contentColor = Color.Red
                            ),
                            contentPadding = PaddingValues(8.dp),
                            onClick = {}
                        ) {
                            Text("Wallet")
                        }

                        Spacer(Modifier.width(16.dp))
                    }
                }

                Row(
                    modifier = Modifier
                        .height(32.dp)
                        .width(searchWidth.value)
                        .clip(shape = RoundedCornerShape(24.dp))
                        .background(color.value)
                        .wrapContentSize(unbounded = true, align = Alignment.CenterStart)
                        .clipToBounds(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                ) {
                    Spacer(modifier = Modifier.width(searchPadding.value))

                    if (isIcon) {
                        Icon(
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { isSearch = !isSearch },
                            painter = painterResource(R.drawable.search_icon),
                            contentDescription = "Search icon",
                            tint = Color.White
                        )
                    } else {
                        TextField(
                            value = searchQuery,
                            onValueChange = { input ->
                                searchQuery = input
                            },
                            leadingIcon = {
                                Icon(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable {
                                            isSearch = !isSearch
                                            searchQuery = ""
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

                if (endSpacerWeight.value != 0F) {
                    Spacer(Modifier.weight(endSpacerWeight.value))
                }
            }

            Image(
                painter = painterResource(R.drawable.logo),
                modifier = Modifier
                    .size(36.dp)
                    .align(Alignment.CenterStart),
                contentDescription = "logo"
            )
        }

        Spacer(
            modifier = Modifier
                .padding(top = 8.dp)
                .height(1.dp)
                .fillMaxWidth()
                .background(Color.Gray)
        )
    }
}

@Preview
@Composable
private fun TopNavBarPreview() {
    MashItTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .systemBarsPadding()
                .background(Color(16, 16, 16))
        ) {
            TopNavBar()
        }
    }
}