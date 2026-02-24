package com.mashiverse.mashit.utils.helpers

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import com.mashiverse.mashit.data.models.ui.DeviceUiType

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun DeviceUiTypeHelper(
    getUiType: (DeviceUiType) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val uiType = when {
        isLandscape && screenHeight <= 480 -> DeviceUiType.PHONE_LANDSCAPE
        screenWidth < 600 -> DeviceUiType.PHONE
        screenWidth < 840 -> DeviceUiType.FOLDABLE
        else -> DeviceUiType.TABLET
    }

    getUiType.invoke(uiType)
}
