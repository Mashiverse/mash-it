package com.mashiverse.mashit

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.reown.android.Core
import com.reown.android.CoreClient
import com.reown.android.relay.ConnectionType
import com.reown.appkit.client.AppKit
import com.reown.appkit.client.Modal
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject


@HiltAndroidApp
class MashItApp : Application(), Configuration.Provider {

    @Inject lateinit var workerFactory: HiltWorkerFactory
    @Inject lateinit var appMetaData: Core.Model.AppMetaData
    @Inject lateinit var polygonChain: Modal.Model.Chain

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .build()

    override fun onCreate() {
        super.onCreate()

        if (Timber.treeCount == 0) Timber.plant(Timber.DebugTree())

        CoreClient.initialize(
            projectId = "64e5fcdc68005f09c6316af60084ddab",
            connectionType = ConnectionType.AUTOMATIC,
            application = this,
            metaData = appMetaData,
            onError = { err -> Timber.tag("GG").e(err.throwable) }
        )

        AppKit.initialize(
            init = Modal.Params.Init(
                core = CoreClient,
                coinbaseEnabled = true,
                includeWalletIds = listOf(
                    "fd20dc426fb37566d803205b19bbc1d4096b248ac04548e3cfb6b3a38bd033aa",
                    "c57ca95b47569778a828d19178114f4db188b89b763c899ba0be274e97267d96"
                )
            ),
            onSuccess = { AppKit.setChains(listOf(polygonChain)) },
            onError = { error -> Timber.tag("GG").e(error.throwable) }
        )
    }
}