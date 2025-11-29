package dev.tymoshenko.mashit.data.local.ds

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.tymoshenko.mashit.utils.USER_WALLET_KEY
import dev.tymoshenko.mashit.utils.USER_WALLET_PREFERENCES
import dev.tymoshenko.mashit.utils.USER_WALLET_TYPE_KEY

val Context.dataStore by preferencesDataStore(
    name = USER_WALLET_PREFERENCES
)

object  PreferencesKeys {
    val WALLET_TYPE = stringPreferencesKey(USER_WALLET_TYPE_KEY)
    val WALLET = stringPreferencesKey(USER_WALLET_KEY)
}