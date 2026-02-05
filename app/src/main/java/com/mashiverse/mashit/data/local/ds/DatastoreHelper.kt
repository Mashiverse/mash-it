package com.mashiverse.mashit.data.local.ds

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mashiverse.mashit.utils.WALLET_KEY
import com.mashiverse.mashit.utils.WALLET_PREFERENCES

val Context.datastore by preferencesDataStore(
    name = WALLET_PREFERENCES
)

object  PreferencesKeys {
    val WALLET = stringPreferencesKey(WALLET_KEY)
}