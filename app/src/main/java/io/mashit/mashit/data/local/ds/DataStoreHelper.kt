package io.mashit.mashit.data.local.ds

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import io.mashit.mashit.utils.USER_WALLET_KEY
import io.mashit.mashit.utils.USER_WALLET_PREFERENCES

val Context.dataStore by preferencesDataStore(
    name = USER_WALLET_PREFERENCES
)

object  PreferencesKeys {
    val WALLET = stringPreferencesKey(USER_WALLET_KEY)
}