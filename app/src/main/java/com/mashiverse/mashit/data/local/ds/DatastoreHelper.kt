package com.mashiverse.mashit.data.local.ds

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mashiverse.mashit.utils.FIRST_LAUNCH_KEY
import com.mashiverse.mashit.utils.NOTIFICATIONS_KEY
import com.mashiverse.mashit.utils.WALLET_KEY
import com.mashiverse.mashit.utils.WALLET_PREFERENCES

val Context.datastore by preferencesDataStore(
    name = WALLET_PREFERENCES
)

object  PreferencesKeys {
    val WALLET = stringPreferencesKey(WALLET_KEY)
    val FIRST_LAUNCH = booleanPreferencesKey(FIRST_LAUNCH_KEY)
    val NOTIFICATIONS = booleanPreferencesKey(NOTIFICATIONS_KEY)
}