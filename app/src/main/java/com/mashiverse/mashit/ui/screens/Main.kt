package com.mashiverse.mashit.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.isImeVisible
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.coinbase.android.nativesdk.CoinbaseWalletSDK
import com.mashiverse.mashit.data.models.dialog.DialogContent
import com.mashiverse.mashit.data.models.wallet.WalletPreferences
import com.mashiverse.mashit.nav.graphs.mainGraph
import com.mashiverse.mashit.nav.routes.MainRoutes
import com.mashiverse.mashit.ui.components.dialogs.Dialog
import com.mashiverse.mashit.ui.components.nav.drawer.NavDrawer
import com.mashiverse.mashit.ui.components.nav.top.TopNavBar
import com.mashiverse.mashit.ui.components.placeholders.Dimmer
import com.mashiverse.mashit.ui.theme.Background
import com.mashiverse.mashit.utils.helpers.PermissionsHelper
import kotlinx.coroutines.launch

@SuppressLint("RestrictedApi", "CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun Main(navController: NavHostController) {

    // Initial config
    val focusManager = LocalFocusManager.current
    val ctx = LocalContext.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val viewModel = hiltViewModel<MainViewModel>()


    // First launch
    val firstLaunch = viewModel.firstLaunchPreferences.collectAsState(false)

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.updateNotifications(isGranted)
    }

    val onFirstLaunchDialogClose = {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            val isGranted = PermissionsHelper.checkNotificationsPermission(ctx)
            viewModel.updateNotifications(isGranted)
        }
        viewModel.setFirstLaunchCompleted()
        viewModel.clearDialog()
    }

    LaunchedEffect(firstLaunch.value) {
        if (firstLaunch.value) {
            viewModel.setDialogContent(
                DialogContent(
                    title = "Important",
                    text = "Please grant notifications permission to be notified about new releases. You can disable this functionality in settings later."
                )
            )
        }
    }


    // Search
    var isSearch by remember { mutableStateOf(false) }
    val onIsSearchChange = { isSearch = !isSearch }

    val searchQuery = remember { mutableStateOf("") }
    val onSearchQueryChange = { input: String ->
        searchQuery.value = input
    }

    val clearSearchQuery = {
        searchQuery.value = ""
        focusManager.clearFocus(true)
        isSearch = false
    }

    LaunchedEffect(navBackStackEntry?.destination?.route) {
        clearSearchQuery.invoke()
    }


    // Base and auth
    val walletPreferences = viewModel.walletPreferences.collectAsState(WalletPreferences(null))
    var clientRef by remember { mutableStateOf<CoinbaseWalletSDK?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val uri = result.data?.data ?: return@rememberLauncherForActivityResult
        clientRef?.handleResponse(uri)
    }

    LaunchedEffect(Unit) {
        clientRef = viewModel.getCoinbaseSdk { intent ->
            launcher.launch(intent)
        }
    }


    // Connect
    val openGooglePlay = {
        val packageName = "org.toshi"
        val uri = "market://details?id=$packageName".toUri()

        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        try {
            ctx.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                "https://play.google.com/store/apps/details?id=$packageName".toUri()
            )
            ctx.startActivity(webIntent)
        }
    }

    val onConnect = {
        if (walletPreferences.value.wallet != null) {
            viewModel.disconnect()
        } else {
            if (clientRef?.isCoinbaseWalletInstalled == true) {
                viewModel.initHandshake(clientRef)
            } else {
                openGooglePlay.invoke()
            }
        }
    }


    // Keyboard
    val isKeyboardVisible = WindowInsets.isImeVisible

    LaunchedEffect(isKeyboardVisible) {
        if (!isKeyboardVisible) {
            focusManager.clearFocus()
            isSearch = false
        }
    }


    // Other
    val isArtists by remember {
        derivedStateOf {
            navBackStackEntry?.destination?.hasRoute<MainRoutes.Artists>() == true
        }
    }
    val dialogContent by remember { viewModel.dialogContent }
    val closeDrawer = { scope.launch { drawerState.close() } }


    // UI
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
                    onConnect = onConnect,
                    searchQuery = searchQuery.value,
                    onSearchQueryChange = onSearchQueryChange,
                    isSearch = isSearch,
                    onIsSearchChange = onIsSearchChange
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
                mainGraph(
                    searchQuery = searchQuery,
                    clearSearchQuery = clearSearchQuery
                )
            }

            if (drawerState.isOpen) {
                Dimmer(onClick = {
                    closeDrawer.invoke()
                })
            }
        }

        if (firstLaunch.value && dialogContent != null) {
            Dialog(
                dialogContent = dialogContent!!,
                onDismissRequest = onFirstLaunchDialogClose
            )
        }
    }
}