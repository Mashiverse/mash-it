package dev.tymoshenko.mashit.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dev.tymoshenko.mashit.nav.graphs.mainGraph
import dev.tymoshenko.mashit.nav.routes.MainRoutes
import dev.tymoshenko.mashit.ui.screens.main.components.nav.NavDrawer
import dev.tymoshenko.mashit.ui.screens.main.components.nav.TopNavBar
import dev.tymoshenko.mashit.ui.theme.Background
import kotlin.math.max

@Composable
fun Main(onDisconnect: () -> Unit) {
    val density = LocalDensity.current
    val config = LocalConfiguration.current

    val screenWidthDp = config.screenWidthDp.dp
    val screenHeightDp = config.screenHeightDp.dp

    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var previousOffset by remember { mutableStateOf(drawerState.currentOffset) }

    val drawerWidth by remember(drawerState) {
        derivedStateOf {
            val delta = drawerState.currentOffset - previousOffset
            previousOffset = drawerState.currentOffset
            delta
        }
    }

    DismissibleNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavDrawer()
        },
        gesturesEnabled = true
    ) {
        Scaffold(
            modifier = Modifier
                .width(screenWidthDp)
                .height(screenHeightDp)
                .background(Background)
                .offset{ IntOffset(x = max(0, drawerWidth.toInt()), y = 0) },
            topBar = {
                TopNavBar(
                    drawerState = drawerState,
                    scope = scope,
                    wallet = "0xd659688366e5a5a6190409dcd4834b3a5b7c88ba",
                    onDisconnect = onDisconnect
                )
            }
        ) { paddingValues ->
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Background)
            )

            NavHost(
                navController = navController,
                startDestination = MainRoutes.Shop,
                modifier = Modifier
                    .padding(paddingValues)
            ) {
                mainGraph(navController)
            }
        }
    }
}