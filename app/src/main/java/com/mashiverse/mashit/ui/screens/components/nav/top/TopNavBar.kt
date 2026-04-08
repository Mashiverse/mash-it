package com.mashiverse.mashit.ui.screens.components.nav.top

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.Cooper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TopNavBar(
    tabName: String,
    drawerState: DrawerState,
    scope: CoroutineScope,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onIsSearchChange: () -> Unit,
    isSearch: Boolean,
    hasSearch: Boolean = true
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            contentAlignment = Alignment.Center
        ) {
            if (hasSearch) {
                TopNavBarActions(
                    isSearch = isSearch,
                    onIsSearchChange = onIsSearchChange,
                    searchQuery = searchQuery,
                    onSearchQueryChange = onSearchQueryChange,
                )
            }

            Row(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnimatedVisibility(
                    visible = !isSearch,
                    enter = fadeIn(tween(durationMillis = 150)),
                    exit = fadeOut(tween(durationMillis = 150))
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            modifier = Modifier
                                .size(32.dp)
                                .align(Alignment.CenterStart)
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

                        Text(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .offset(y = 2.dp),
                            text = "mash-it · $tabName",
                            fontSize = 20.sp,
                            fontFamily = Cooper,
                            color = ContentAccentColor
                        )
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(1.dp)
                .background(Color.DarkGray)
        )

        Spacer(Modifier.height(16.dp))
    }
}