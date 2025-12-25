package dev.tymoshenko.mashit.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dev.tymoshenko.mashit.nav.graphs.mainGraph
import dev.tymoshenko.mashit.nav.routes.MainRoutes
import dev.tymoshenko.mashit.ui.screens.main.components.nav.drawer.NavDrawer
import dev.tymoshenko.mashit.ui.screens.main.components.nav.top.TopNavBar
import dev.tymoshenko.mashit.ui.theme.Background
import kotlinx.coroutines.launch

@Composable
fun Main(onDisconnect: () -> Unit) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    DismissibleNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavDrawer(navController = navController)
        },
        gesturesEnabled = true
    ) {
        Box {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Background),
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

                if (drawerState.isOpen) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null  // disables ripple
                            ) {
                                scope.launch { drawerState.close() }
                            }
                    )
                }
            }
        }
    }
}