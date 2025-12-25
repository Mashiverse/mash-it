package dev.tymoshenko.mashit.ui.screens.main.components.nav.top

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.tymoshenko.mashit.R
import dev.tymoshenko.mashit.ui.theme.DrawerPaddingSize
import dev.tymoshenko.mashit.ui.theme.IconSize
import dev.tymoshenko.mashit.ui.theme.NavBarHeight
import dev.tymoshenko.mashit.ui.theme.SmallPaddingSize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TopNavBar(
    wallet: String,
    onDisconnect: () -> Unit,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    var isSearch by remember {
        mutableStateOf(false)
    }
    val onIsSearchChange = remember {
        { isSearch = !isSearch }
    }

    var searchQuery by remember { mutableStateOf("") }
    val onSearchQueryChange = remember {
        { input: String ->
            searchQuery = input
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
    ) {
        Spacer(modifier = Modifier.height(DrawerPaddingSize))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(NavBarHeight),
            contentAlignment = Alignment.Center
        ) {
            TopNavBarActions(
                isSearch = isSearch,
                onIsSearchChange = onIsSearchChange,
                searchQuery = searchQuery,
                onSearchQueryChange = onSearchQueryChange,
                wallet = wallet,
                onDisconnect = onDisconnect
            )

            Row(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(horizontal = DrawerPaddingSize),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(IconSize)
                        .clip(CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        modifier = Modifier
                            .size(IconSize)
                            .clickable(onClick = {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }),
                        tint = Color.White,
                        contentDescription = "menu icon"
                    )
                }

                Spacer(modifier = Modifier.width(SmallPaddingSize))

                AnimatedVisibility(
                    visible = !isSearch,
                    enter = fadeIn(tween(durationMillis = 150)),
                    exit = fadeOut(tween(durationMillis = 75)) //slideOutHorizontally(targetOffsetX = { it -> -it* 2 }),
                ) {
                    Image(
                        painter = painterResource(R.drawable.logo),
                        modifier = Modifier
                            .size(NavBarHeight),
                        contentDescription = "logo"
                    )
                }
            }

        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height((1.3).dp)
                .background(Color.DarkGray)
        )
    }
}