package com.mashiverse.mashit.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.mashiverse.mashit.ui.theme.DarkSystemBarStyle
import com.reown.android.Core
import com.reown.android.CoreClient
import com.reown.android.relay.ConnectionType
import com.reown.appkit.client.AppKit
import com.reown.appkit.client.Modal
import com.reown.appkit.ui.components.internal.AppKitComponent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Timber.treeCount == 0) {
            Timber.plant(Timber.DebugTree())
        }

        enableEdgeToEdge(
            statusBarStyle = DarkSystemBarStyle,
            navigationBarStyle = DarkSystemBarStyle
        )

        val polygonChain = Modal.Model.Chain(
            chainName = "Polygon",
            chainNamespace = "eip155",
            chainReference = "137",
            requiredMethods = listOf("eth_sendTransaction", "personal_sign"),
            optionalMethods = emptyList(),
            events = listOf("chainChanged", "accountsChanged"),
            token = Modal.Model.Token(
                name = "USD Coin",
                symbol = "USDC", // The symbol shown in the UI
                decimal = 6      // IMPORTANT: USDC uses 6 decimals
            ),
            rpcUrl = "https://polygon-rpc.com",
            blockExplorerUrl = "https://polygonscan.com"
        )

        val connectionType = ConnectionType.AUTOMATIC
        val projectId =
            "64e5fcdc68005f09c6316af60084ddab" // Get Project ID at https://dashboard.reown.com/
        val appMetaData = Core.Model.AppMetaData(
            name = "mash-it",
            description = "Kotlin AppKit Implementation",
            url = "https://www.mash-it.io",
            icons = listOf("https://gblobscdn.gitbook.com/spaces%2F-LJJeCjcLrr53DcT1Ml7%2Favatar.png?alt=media"),
            redirect = "mashit://request",
        )

        CoreClient.initialize(
            projectId = projectId,
            connectionType = connectionType,
            application = this.application,
            metaData = appMetaData,
            onError = { err -> Timber.tag("GG").d(err.component1()) })


        AppKit.initialize(
            init = Modal.Params.Init(
                core = CoreClient,
                coinbaseEnabled = true, // This ADDS Coinbase Wallet to the UI
                recommendedWalletsIds = listOf(
                    "c57ca40fa49151d97d19092181013583ad0f077e895f5b8c999a34d74e1e81b1" // This ADDS MetaMask
                ),
                excludedWalletIds = listOf("ALL") // This HIDES everything else
            ),
            onSuccess = {
                Timber.tag("GG").d(AppKit.getSession().toString())
            },
            onError = { error ->
                Timber.tag("GG").d(error.component1())
            },
        )

        val appKitModalDelegate = object : AppKit.ModalDelegate {
            override fun onSessionApproved(approvedSession: Modal.Model.ApprovedSession) {
                Timber.tag("GG").d(approvedSession.toString())
            }

            override fun onSessionRejected(rejectedSession: Modal.Model.RejectedSession) {
                // Triggered when receives the session rejection from wallet
            }

            override fun onSessionUpdate(updatedSession: Modal.Model.UpdatedSession) {
                // Triggered when receives the session update from wallet
            }

            override fun onSessionExtend(session: Modal.Model.Session) {
                // Triggered when receives the session extend from wallet
            }

            override fun onSessionEvent(sessionEvent: Modal.Model.SessionEvent) {
                // Triggered when the peer emits events that match the list of events agreed upon session settlement
            }

            override fun onSessionDelete(deletedSession: Modal.Model.DeletedSession) {
                // Triggered when receives the session delete from wallet
            }

            override fun onSessionRequestResponse(response: Modal.Model.SessionRequestResponse) {
                // Triggered when receives the session request response from wallet
            }

            override fun onProposalExpired(proposal: Modal.Model.ExpiredProposal) {
                // Triggered when a proposal becomes expired
            }

            override fun onRequestExpired(request: Modal.Model.ExpiredRequest) {
                // Triggered when a request becomes expired
            }

            override fun onConnectionStateChange(state: Modal.Model.ConnectionState) {
                //Triggered whenever the connection state is changed
            }

            override fun onError(error: Modal.Model.Error) {
                // Triggered whenever there is an issue inside the SDK
            }
        }


        AppKit.setDelegate(appKitModalDelegate)
        AppKit.setChains(chains = listOf(polygonChain))
        AppKit.register(this)

        Timber.tag("GG").d(AppKit.getAccount().toString())

        setContent {

            navController = rememberNavController()
            val modalSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            val coroutineScope = rememberCoroutineScope()



            Column(
                modifier = Modifier.systemBarsPadding()
            ) {
                AppKitComponent(
                    shouldOpenChooseNetwork = false,
                    closeModal = { coroutineScope.launch { modalSheetState.hide() } }
                )

                Button(onClick = {
                    AppKit.disconnect(
                        onSuccess = {},
                        onError = {}
                    )
                }) {
                    Text("Disconnect")
                }

            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        navController.handleDeepLink(intent)
    }
}