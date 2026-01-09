package io.mashit.mashit.ui.screens.main

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.coinbase.android.nativesdk.message.request.Account
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.coinbase.android.nativesdk.CoinbaseWalletSDK
import com.coinbase.android.nativesdk.message.request.RequestContent
import com.coinbase.android.nativesdk.message.request.Web3JsonRPC
import com.coinbase.android.nativesdk.message.response.ActionResult
import com.coinbase.android.nativesdk.message.response.ResponseResult
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.reown.appkit.client.AppKit
import com.reown.appkit.client.Modal
import com.reown.appkit.ui.AppKitTheme
import com.reown.appkit.ui.appKitGraph
import com.reown.appkit.ui.openAppKit

import io.mashit.mashit.nav.graphs.mainGraph
import io.mashit.mashit.nav.routes.MainRoutes
import io.mashit.mashit.ui.screens.main.nav.drawer.NavDrawer
import io.mashit.mashit.ui.screens.main.nav.top.TopNavBar
import io.mashit.mashit.ui.theme.Background
import kotlinx.coroutines.launch
import sendBasicTransactionSafe
import timber.log.Timber
import androidx.core.net.toUri
import kotlinx.coroutines.delay

@SuppressLint("RestrictedApi")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialNavigationApi::class)
@Composable
fun Main() {

    val ctx = LocalContext.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberNavController(bottomSheetNavigator)

    val clientRef = remember { mutableStateOf<CoinbaseWalletSDK?>(null) }

    // 2️⃣ Create the launcher first
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val uri = result.data?.data ?: return@rememberLauncherForActivityResult
        clientRef.value?.handleResponse(uri) // safe, client exists now
    }

    val accountRef = remember { mutableStateOf<Account?>(null) }

    // 3️⃣ Initialize client with launcher
    LaunchedEffect(Unit) {
        clientRef.value = CoinbaseWalletSDK(
            appContext = ctx.applicationContext,
            domain = "https://www.mash-it.io".toUri(),
            openIntent = { intent: Intent -> launcher.launch(intent) }
        )

        delay(3000)

        val requestAccount = Web3JsonRPC.RequestAccounts().action()
        val handShakeActions = listOf(requestAccount)


        clientRef.value?.initiateHandshake(
            initialActions = handShakeActions
        ) { result: Result<List<ActionResult>>, account: Account? ->
            result.onSuccess { actionResults: List<ActionResult> ->
                accountRef.value = account
            }
            result.onFailure { err ->
                Timber.tag("WEB3_CHECK").d(err)
            }
        }

        delay(3000)


    }


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
//                                navController.openAppKit(
//                                    shouldOpenChooseNetwork = false,
//                                    onError = { error ->
//                                        Timber.tag("GG").e(error)
//                                    }
//                                )

                                val signTypedDataV3 = Web3JsonRPC.SignTypedDataV3(
                                    "0xd659688366e5a5a6190409dcd4834b3a5b7c88ba", // address
                                    "{\n" +
                                            "  \"types\": {\n" +
                                            "    \"EIP712Domain\": [\n" +
                                            "      {\"name\": \"name\", \"type\": \"string\"},\n" +
                                            "      {\"name\": \"version\", \"type\": \"string\"},\n" +
                                            "      {\"name\": \"chainId\", \"type\": \"uint256\"},\n" +
                                            "      {\"name\": \"verifyingContract\", \"type\": \"address\"}\n" +
                                            "    ],\n" +
                                            "    \"Transaction\": [\n" +
                                            "      {\"name\": \"from\", \"type\": \"address\"},\n" +
                                            "      {\"name\": \"to\", \"type\": \"address\"},\n" +
                                            "      {\"name\": \"value\", \"type\": \"uint256\"},\n" +
                                            "      {\"name\": \"nonce\", \"type\": \"uint256\"}\n" +
                                            "    ]\n" +
                                            "  },\n" +
                                            "  \"primaryType\": \"Transaction\",\n" +
                                            "  \"domain\": {\n" +
                                            "    \"name\": \"EtherTransfer\",\n" +
                                            "    \"version\": \"1\",\n" +
                                            "    \"chainId\": 1,\n" +
                                            "    \"verifyingContract\": \"0x0000000000000000000000000000000000000000\"\n" +
                                            "  },\n" +
                                            "  \"message\": {\n" +
                                            "    \"from\": \"0xd659688366e5a5a6190409dcd4834b3a5b7c88ba\",\n" +
                                            "    \"to\": \"0xd659688366e5a5a6190409dcd4834b3a5b7c88ba\",\n" +
                                            "    \"value\": \"1000000000000000000\",\n" +
                                            "    \"nonce\": 1\n" +
                                            "  }\n" +
                                            "}" // typed data JSON
                                ).action()
                                val requestActions = listOf(signTypedDataV3)

                                clientRef.value?.makeRequest(request = RequestContent.Request(actions = requestActions, account = accountRef.value)) { result ->
                                    result.fold(
                                        onSuccess = { returnValues ->
                                            Timber.tag("WEB3_CHECK").d(returnValues.toString())
                                        },
                                        onFailure = { err ->
                                            Timber.tag("WEB3_CHECK").d(err)
                                        }
                                    )
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