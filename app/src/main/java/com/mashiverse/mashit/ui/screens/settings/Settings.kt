package com.mashiverse.mashit.ui.screens.settings

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.PaddingSize
import com.mashiverse.mashit.ui.theme.SmallPaddingSize
import com.mashiverse.mashit.utils.helpers.PermissionsHelper

@Composable
fun Settings() {
    val activity = LocalActivity.current
    val ctx = LocalContext.current
    val viewModel = hiltViewModel<SettingsViewModel>()

    val notifications = viewModel.notificationsPreferences.collectAsState(false)
    val checked by remember(notifications.value) { mutableStateOf(notifications.value) }

    val updateNotifications = { enabled: Boolean ->
        if (enabled) {
            if (!PermissionsHelper.checkNotificationsPermission(ctx)) {
                activity?.let {
                    PermissionsHelper.getNotificationsPermission(ctx, activity)
                }
            }
        }
        viewModel.updateNotifications(enabled)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(PaddingSize),
        contentAlignment = Alignment.TopStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Notifications:", fontSize = 14.sp, color = ContentAccentColor, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.width(SmallPaddingSize))

            Switch(
                checked = checked,
                onCheckedChange = {
                    updateNotifications.invoke(!checked)
                }
            )
        }
    }
}