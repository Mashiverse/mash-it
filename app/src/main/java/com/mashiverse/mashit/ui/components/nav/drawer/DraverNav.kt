package com.mashiverse.mashit.ui.components.nav.drawer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DrawerState
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.mashiverse.mashit.data.models.nav.navItems

import com.mashiverse.mashit.ui.theme.Background
import com.mashiverse.mashit.ui.theme.ContainerColor
import com.mashiverse.mashit.ui.theme.ContentColor
import com.mashiverse.mashit.ui.theme.ContentContainerHeight
import com.mashiverse.mashit.ui.theme.ContentContainerShape
import com.mashiverse.mashit.ui.theme.PaddingSize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun DrawerNav(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    var selectedDest by rememberSaveable {
        mutableIntStateOf(0)
    }

    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(PaddingSize),
    ) {
        navItems.forEachIndexed { i, navItem ->
            NavigationDrawerItem(
                modifier = Modifier
                    .height(ContentContainerHeight),
                onClick = {
                    navController.navigate(route = navItem.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                    selectedDest = i
                    scope.launch {
                        drawerState.close()
                    }
                },
                selected = i == selectedDest,
                label = {
                    Text(text = navItem.label, fontSize = 16.sp)
                },
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = ContainerColor,
                    unselectedContainerColor = Background,
                    selectedTextColor = ContentColor,
                    unselectedTextColor = ContentColor
                ),
                shape = ContentContainerShape
            )
        }
    }
}