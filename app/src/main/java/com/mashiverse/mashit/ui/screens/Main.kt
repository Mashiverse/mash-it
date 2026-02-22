package com.mashiverse.mashit.ui.screens

import android.annotation.SuppressLint
import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.coinbase.android.nativesdk.CoinbaseWalletSDK
import com.mashiverse.mashit.data.models.wallet.WalletPreferences
import com.mashiverse.mashit.nav.graphs.mainGraph
import com.mashiverse.mashit.nav.routes.MainRoutes
import com.mashiverse.mashit.ui.screens.components.dialogs.Dialog
import com.mashiverse.mashit.ui.screens.components.nav.drawer.NavDrawer
import com.mashiverse.mashit.ui.screens.components.nav.top.TopNavBar
import com.mashiverse.mashit.ui.theme.Background
import kotlinx.coroutines.launch

@SuppressLint("RestrictedApi", "CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Main(navController: NavHostController) {
    val viewModel = hiltViewModel<Web3ViewModel>()
    val ctx = LocalContext.current

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val isArtists by remember {
        derivedStateOf {
            navBackStackEntry?.destination?.hasRoute<MainRoutes.Artists>() == true
        }
    }

    var searchQuery = remember { mutableStateOf("") }
    val onSearchQueryChange = remember {
        { input: String ->
            searchQuery.value = input
        }
    }
    val clearSearchQuery = {
        searchQuery.value = ""
    }

    val dialogContent by remember {
        viewModel.dialogContent
    }

    val openGooglePlay = {
        val packageName = "org.toshi"
        try {
            val intent =
                Intent(Intent.ACTION_VIEW, "market://details?id=$packageName".toUri()).apply {
                    setPackage("com.android.vending")
                }
            ctx.startActivity(intent)
        } catch (e: Exception) {
            val intent = Intent(
                Intent.ACTION_VIEW,
                "https://play.google.com/store/apps/details?id=$packageName".toUri()
            )
            ctx.startActivity(intent)
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
                            if (clientRef?.isCoinbaseWalletInstalled == true) {
                                viewModel.initHandshake(clientRef)
                            } else {
                                openGooglePlay.invoke()
                            }
                        }
                    },
                    searchQuery = searchQuery.value,
                    onSearchQueryChange = onSearchQueryChange
                )
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = MainRoutes.Shop(listingId = null),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                mainGraph(searchQuery = searchQuery)
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

        if (dialogContent != null) {
            // TODO: rework error dialog
            Dialog(dialogContent!!) {
                viewModel.clearDialog()
            }
        }
    }
}