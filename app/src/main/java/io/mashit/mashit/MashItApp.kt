package io.mashit.mashit

import android.app.Application
import com.reown.android.Core
import com.reown.android.CoreClient
import com.reown.android.relay.ConnectionType
import com.reown.appkit.client.AppKit
import com.reown.appkit.client.Modal
import com.reown.appkit.presets.AppKitChainsPresets
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MashItApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val connectionType = ConnectionType.AUTOMATIC
        val projectId = BuildConfig.REOWN_PROJECT_ID

        val appMetaData = Core.Model.AppMetaData(
            name = "Mash It",
            description = "Mashing platform",
            url = "https://mash-it.io",
            icons = listOf("https://www.mash-it.io/img/transparent_logo_p.png?v=d38f16da3dbbe9535ea894f4699c1a2386cfdcad"),
            redirect = "mashi://"
        )

        CoreClient.initialize(
            projectId = "a02c5e102a8b6292ac8ad8df578b3bba",
            connectionType = connectionType,
            application = this,
            metaData = appMetaData,
        ) {
            Timber.d("CoreClient initialized")
        }

        AppKit.initialize(
            init = Modal.Params.Init(
                CoreClient, coinbaseEnabled = true, includeWalletIds = listOf(
                    "c57ca95b47569778a828d19178114f4db188b89b763c899ba0be274e97267d96", // MetaMask
                    "fd20dc426fb37566d803205b19bbc1d4096b248ac04548e3cfb6b3a38bd033aa" // Base
                )
            ),
            onSuccess = {
                val chains = listOfNotNull(
                    AppKitChainsPresets.ethChains["137"],  // Polygon
                )
                AppKit.setChains(chains)
            },
            onError = { error ->
                Timber.tag("AppKit").e("Initialization error: ${error.throwable.message}")
            }
        )
    }
}