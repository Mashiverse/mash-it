package com.mashiverse.mashit.ui.screens.settings

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.mashiverse.mashit.ui.theme.Padding
import com.mashiverse.mashit.ui.theme.SmallPadding
import com.mashiverse.mashit.utils.helpers.sys.checkNotificationsPermission
import com.mashiverse.mashit.utils.helpers.sys.getNotificationsPermission

@Composable
fun Settings() {
    val activity = LocalActivity.current
    val ctx = LocalContext.current
    val viewModel = hiltViewModel<SettingsViewModel>()

    val notifications = viewModel.notificationsFlow.collectAsState(false)
    val isNotifications by remember(notifications.value) { mutableStateOf(notifications.value) }

    val specialDrops = viewModel.specialDropsFlow.collectAsState(false)
    val isSpecialDrops by remember(specialDrops.value) { mutableStateOf(specialDrops.value) }

    val updateNotifications = { enabled: Boolean ->
        if (enabled) {
            if (!checkNotificationsPermission(ctx)) {
                activity?.let {
                    getNotificationsPermission(ctx, activity)
                }
            }
        }
        viewModel.updateNotifications(enabled)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Padding),
    ) {
        CheckRow(
            title = "Opt in to new releases",
            checked = isNotifications
        ) { checked ->
            updateNotifications.invoke(checked)
        }

        Spacer(modifier = Modifier.height(SmallPadding))

        CheckRow(
            title = "Disable special drops",
            checked = isSpecialDrops
        ) { checked ->
            viewModel.updateSpecialDrops(checked)
        }
    }
}