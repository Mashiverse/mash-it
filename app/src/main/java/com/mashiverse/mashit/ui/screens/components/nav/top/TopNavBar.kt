package com.mashiverse.mashit.ui.screens.components.nav.top

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mashiverse.mashit.R
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.DrawerPaddingSize
import com.mashiverse.mashit.ui.theme.NavBarHeight
import com.mashiverse.mashit.ui.theme.SmallIconSize
import com.mashiverse.mashit.ui.theme.SmallPaddingSize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TopNavBar(
    isArtists: Boolean,
    wallet: String?,
    onConnect: () -> Unit,
    drawerState: DrawerState,
    scope: CoroutineScope,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onIsSearchChange: () -> Unit,
    isSearch: Boolean
) {
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
                    if (isArtists) {
                        Image(
                            painter = painterResource(R.drawable.text_logo),
                            modifier = Modifier
                                .height(NavBarHeight)
                                .width((48 * 2).dp),
                            contentDescription = null
                        )
                    } else {
                        Image(
                            painter = painterResource(R.drawable.logo),
                            modifier = Modifier
                                .size(NavBarHeight),
                            contentDescription = "logo"
                        )
                    }
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