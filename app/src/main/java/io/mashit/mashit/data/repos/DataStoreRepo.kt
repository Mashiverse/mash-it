package io.mashit.mashit.data.repos

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import io.mashit.mashit.data.local.ds.PreferencesKeys
import io.mashit.mashit.data.models.wallet.WalletPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreRepo @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    val walletPreferencesFlow: Flow<WalletPreferences> =
        dataStore.data
            .catch { e ->
                if (e is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw e
                }
            }
            .map { preferences ->
                WalletPreferences(
                    wallet = preferences[PreferencesKeys.WALLET]
                )
            }

    suspend fun updateWallet(wallet: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.WALLET] = wallet
        }
    }

    suspend fun removeWallet() {
        dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.WALLET)
        }
    }
}