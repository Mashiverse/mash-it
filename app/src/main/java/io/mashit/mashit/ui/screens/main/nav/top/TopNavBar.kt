package io.mashit.mashit.ui.screens.main.nav.top

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
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.mashit.mashit.R
import io.mashit.mashit.ui.theme.ContentAccentColor
import io.mashit.mashit.ui.theme.DrawerPaddingSize
import io.mashit.mashit.ui.theme.NavBarHeight
import io.mashit.mashit.ui.theme.SmallIconSize
import io.mashit.mashit.ui.theme.SmallPaddingSize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TopNavBar(
    wallet: String,
    onConnect: () -> Unit,
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
                onConnect = onConnect
            )

            Row(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(horizontal = DrawerPaddingSize),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(SmallIconSize)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.menu_icon),
                        modifier = Modifier
                            .size(SmallIconSize)
                            .clickable(onClick = {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }),
                        tint = ContentAccentColor,
                        contentDescription = "menu icon"
                    )
                }

                Spacer(modifier = Modifier.width(SmallPaddingSize))

                AnimatedVisibility(
                    visible = !isSearch,
                    enter = fadeIn(tween(durationMillis = 150)),
                    exit = fadeOut(tween(durationMillis = 75))
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