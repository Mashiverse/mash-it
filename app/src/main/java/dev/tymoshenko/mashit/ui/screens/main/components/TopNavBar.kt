package dev.tymoshenko.mashit.ui.screens.main.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.tymoshenko.mashit.R
import dev.tymoshenko.mashit.ui.theme.MashItTheme

@Composable
fun TopNavBar() {
    var isSearch by remember {
        mutableStateOf(false)
    }

    val endSpacerWeight = animateFloatAsState(
        targetValue = if (!isSearch) {
            0F
        } else {
            1F
        },
        tween(delayMillis = 300)
    )

    val searchWidth = animateDpAsState(
        targetValue = if (isSearch) {
            256.dp
        } else {
            48.dp
        },
        tween(delayMillis = 300)
    )

    Box(Modifier.fillMaxWidth()) {
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
                Button(onClick = {}) {
                    Text("Wallet")
                }
            }

            Row(
                modifier = Modifier
                    .height(48.dp)
                    .width(searchWidth.value)
                    .background(Color.Red)
                    .clickable {
                        isSearch = !isSearch
                    }
                    .clipToBounds(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                Text(
                    "*icon*   fsadgsdgsd42",
                    modifier = Modifier.wrapContentSize(
                        align = Alignment.CenterStart,
                        unbounded = true
                    )
                )
            }

            if (endSpacerWeight.value != 0F) {
                Spacer(Modifier.weight(endSpacerWeight.value))
            }
        }

        Image(
            painter = painterResource(R.drawable.ic_launcher_foreground),
            modifier = Modifier
                .size(48.dp)
                .background(Color.Cyan),
            contentDescription = "logo"
        )
    }
}

@Preview
@Composable
private fun TopNavBarPreview() {
    MashItTheme(darkTheme = true) {
        Box(modifier = Modifier
            .systemBarsPadding()
            .background(Color(16, 16, 16))) {
            TopNavBar()
        }
    }
}