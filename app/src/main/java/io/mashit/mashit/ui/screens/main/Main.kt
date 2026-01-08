package io.mashit.mashit.ui.screens.main

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.coinbase.android.nativesdk.CoinbaseWalletSDK
import com.coinbase.android.nativesdk.message.request.Web3JsonRPC
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.reown.appkit.client.AppKit
import com.reown.appkit.client.Modal
import com.reown.appkit.ui.AppKitTheme
import com.reown.appkit.ui.appKitGraph
import com.reown.appkit.ui.openAppKit
import com.tinder.scarlet.SideEffect
import io.mashit.mashit.nav.graphs.mainGraph
import io.mashit.mashit.nav.routes.MainRoutes
import io.mashit.mashit.ui.screens.main.nav.drawer.NavDrawer
import io.mashit.mashit.ui.screens.main.nav.top.TopNavBar
import io.mashit.mashit.ui.theme.Background
import kotlinx.coroutines.launch
import okhttp3.internal.platform.PlatformRegistry.applicationContext
import timber.log.Timber

@SuppressLint("RestrictedApi")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialNavigationApi::class)
@Composable
fun Main() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberNavController(bottomSheetNavigator)



    AppKitTheme {
        ModalBottomSheetLayout(
            bottomSheetNavigator = bottomSheetNavigator,
            ) {
            DismissibleNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    NavDrawer(
                        navController = navController,
                        drawerState = drawerState,
                        scope = scope
                    )
                },
                gesturesEnabled = true
            ) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Background,
                    topBar = {
                        TopNavBar(
                            drawerState = drawerState,
                            scope = scope,
                            wallet = AppKit.getAccount()?.address ?: "123124",
                            onConnect = {
                                // ✅ Open AppKit bottom sheet safely
                                navController.openAppKit(
                                    shouldOpenChooseNetwork = false,
                                    onError = { error ->
                                        Timber.tag("GG").e(error)
                                    }
                                )
                            },
                        )
                    }
                ) { paddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = MainRoutes.Mashup,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        mainGraph(navController)
                        appKitGraph(navController)
                    }
                    if (drawerState.isOpen) {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    scope.launch { drawerState.close() }
                                }
                        )
                    }
                }
            }
        }
    }
}