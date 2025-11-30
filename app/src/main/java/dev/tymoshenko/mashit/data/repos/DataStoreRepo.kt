package dev.tymoshenko.mashit.data.repos

import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import dev.tymoshenko.mashit.data.local.ds.PreferencesKeys
import dev.tymoshenko.mashit.data.models.WalletPreferences
import dev.tymoshenko.mashit.data.models.WalletType
import dev.tymoshenko.mashit.data.models.toWalletTypeEnum
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreRepo @Inject constructor(
    val dataStore: DataStore<Preferences>
) {
    val walletPreferencesFlow = dataStore.data
        .catch { e ->
            // TODO
            WalletPreferences(null, null)
        }
        .map { preferences ->
            val walletType = preferences[PreferencesKeys.WALLET_TYPE]?.toWalletTypeEnum()
            val wallet = preferences[PreferencesKeys.WALLET]
            WalletPreferences(walletType, wallet)
        }

    suspend fun updateWallet(walletType: WalletType, wallet: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.WALLET_TYPE] = walletType.name
            preferences[PreferencesKeys.WALLET] = wallet
        }
    }

    suspend fun removeWallet() {
        dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.WALLET)
            preferences.remove(PreferencesKeys.WALLET_TYPE)
        }
    }
}