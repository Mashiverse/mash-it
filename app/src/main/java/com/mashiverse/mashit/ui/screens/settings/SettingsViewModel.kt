package com.mashiverse.mashit.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.mashiverse.mashit.data.repos.DatastoreRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStoreRepo: DatastoreRepo,
) : ViewModel() {
    val notificationsPreferences = dataStoreRepo.notificationsPreferencesFlow

    fun updateNotifications(enabled: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (enabled) {
                FirebaseMessaging.getInstance().subscribeToTopic("all_users")
            } else {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("all_users")
            }

            dataStoreRepo.updateNotifications(enabled)
        }
    }
}