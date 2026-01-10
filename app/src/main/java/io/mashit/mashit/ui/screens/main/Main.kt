package io.mashit.mashit.ui.screens.main

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.coinbase.android.nativesdk.CoinbaseWalletSDK
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import io.mashit.mashit.data.models.wallet.WalletPreferences
import io.mashit.mashit.nav.graphs.mainGraph
import io.mashit.mashit.nav.routes.MainRoutes
import io.mashit.mashit.ui.screens.main.nav.drawer.NavDrawer
import io.mashit.mashit.ui.screens.main.nav.top.TopNavBar
import io.mashit.mashit.ui.theme.Background
import kotlinx.coroutines.launch

@SuppressLint("RestrictedApi")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialNavigationApi::class)
@Composable
fun Main() {
    val viewModel = hiltViewModel<Web3ViewModel>()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()

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
                startDestination = MainRoutes.Mashup,
                modifier = Modifier
                    .fillMaxSize()
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
                            indication = null
                        ) {
                            scope.launch { drawerState.close() }
                        }
                )
            }
        }
    }
}