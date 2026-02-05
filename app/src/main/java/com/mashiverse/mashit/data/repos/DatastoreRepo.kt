package com.mashiverse.mashit.data.repos

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.mashiverse.mashit.data.local.ds.PreferencesKeys
import com.mashiverse.mashit.data.models.wallet.WalletPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatastoreRepo @Inject constructor(
    private val datastore: DataStore<Preferences>
) {

    val walletPreferencesFlow: Flow<WalletPreferences> =
        datastore.data
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
        datastore.edit { preferences ->
            preferences[PreferencesKeys.WALLET] = wallet
        }
    }

    suspend fun removeWallet() {
        datastore.edit { preferences ->
            preferences.remove(PreferencesKeys.WALLET)
        }
    }
}