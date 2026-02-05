package com.mashiverse.mashit.ui.screens

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.coinbase.android.nativesdk.CoinbaseWalletSDK
import com.mashiverse.mashit.data.models.wallet.WalletPreferences
import com.mashiverse.mashit.nav.graphs.mainGraph
import com.mashiverse.mashit.nav.routes.MainRoutes
import com.mashiverse.mashit.ui.screens.nav.drawer.NavDrawer
import com.mashiverse.mashit.ui.screens.nav.top.TopNavBar
import com.mashiverse.mashit.ui.theme.Background
import com.mashiverse.mashit.utils.helpers.SoldHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

@SuppressLint("RestrictedApi", "CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Main() {
    val viewModel = hiltViewModel<Web3ViewModel>()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val isArtists by remember {
        derivedStateOf {
            navBackStackEntry?.destination?.hasRoute<MainRoutes.Artists>() == true
        }
    }

    var clientRef by remember { mutableStateOf<CoinbaseWalletSDK?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val uri = result.data?.data ?: return@rememberLauncherForActivityResult
        clientRef?.handleResponse(uri)
    }

    val walletPreferences = viewModel.walletPreferences.collectAsState(WalletPreferences(null))
    LaunchedEffect(Unit) {
        clientRef = viewModel.getCoinbaseSdk { intent ->
            launcher.launch(intent)
        }
    }

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
                    isArtists = isArtists,
                    drawerState = drawerState,
                    scope = scope,
                    wallet = walletPreferences.value.wallet,
                    onConnect = {
                        if (walletPreferences.value.wallet != null) {
                            viewModel.disconnect()
                        } else {
                            viewModel.initHandshake(clientRef)
                        }
                    },
                )
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = MainRoutes.Shop,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                mainGraph()
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