package com.mashiverse.mashit.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
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
import com.mashiverse.mashit.ui.dialogs.Dialog
import com.mashiverse.mashit.ui.nav.drawer.NavDrawer
import com.mashiverse.mashit.ui.nav.top.TopNavBar
import com.mashiverse.mashit.ui.theme.Background
import com.mashiverse.mashit.utils.helpers.sys.checkNotificationsPermission
import kotlinx.coroutines.launch

@SuppressLint("RestrictedApi", "CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun Main(navController: NavHostController) {
    val viewModel = hiltViewModel<MainViewModel>()
    val focusManager = LocalFocusManager.current
    val ctx = LocalContext.current

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val tabName by remember {
        derivedStateOf {
            val name = when {
                navBackStackEntry?.destination?.hasRoute<MainRoutes.Shop>() == true -> "shop"
                navBackStackEntry?.destination?.hasRoute<MainRoutes.Collection>() == true -> "collection"
                navBackStackEntry?.destination?.hasRoute<MainRoutes.Mashup>() == true -> "mashup"
                navBackStackEntry?.destination?.hasRoute<MainRoutes.Settings>() == true -> "settings"
                else -> "artists"
            }
            name
        }
    }

    val hasSearch by remember {
        derivedStateOf {
            navBackStackEntry?.destination?.hasRoute<MainRoutes.Artists>() != true
        }
    }

    val searchQuery = remember { mutableStateOf("") }
    val onSearchQueryChange = remember {
        { input: String ->
            searchQuery.value = input
        }
    }

    var isSearch by remember {
        mutableStateOf(false)
    }

    val clearSearchQuery = {
        searchQuery.value = ""
        focusManager.clearFocus(true)
        isSearch = false
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
        } catch (_: Exception) {
            val intent = Intent(
                Intent.ACTION_VIEW,
                "https://play.google.com/store/apps/details?id=$packageName".toUri()
            )
            ctx.startActivity(intent)
        }
    }

    val onIsSearchChange = remember {
        { isSearch = !isSearch }
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

    val firstLaunch = viewModel.firstLaunchPreferences.collectAsState(false)

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

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.updateNotifications(isGranted)
    }

    val onFirstLaunchDialogClose = {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            val isGranted = checkNotificationsPermission(ctx)
            viewModel.updateNotifications(isGranted)
        }
        viewModel.setFirstLaunchCompleted()
        viewModel.clearDialog()
    }

    val isKeyboardVisible = WindowInsets.isImeVisible

    LaunchedEffect(isKeyboardVisible) {
        if (!isKeyboardVisible) {
            focusManager.clearFocus()
            isSearch = false
        }
    }

    LaunchedEffect(navBackStackEntry?.destination?.route) {
        clearSearchQuery.invoke()
    }

    DismissibleNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavDrawer(
                navController = navController,
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
            )
        },
        gesturesEnabled = true
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Background,
            topBar = {
                TopNavBar(
                    tabName = tabName,
                    drawerState = drawerState,
                    scope = scope,
                    searchQuery = searchQuery.value,
                    onSearchQueryChange = onSearchQueryChange,
                    isSearch = isSearch,
                    onIsSearchChange = onIsSearchChange,
                    hasSearch = hasSearch
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
                    clearSearchQuery = clearSearchQuery,
                    navController = navController
                )
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

        if (firstLaunch.value && dialogContent != null) {
            // TODO: rework error dialog
            Dialog(dialogContent!!) {
                onFirstLaunchDialogClose.invoke()
            }
        }
    }
}