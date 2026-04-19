package com.mashiverse.mashit.ui.nav.drawer

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mashiverse.mashit.ui.default.buttons.DiscordButton
import com.mashiverse.mashit.ui.default.buttons.RedditButton

@Composable
fun DrawerRedirect() {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Spacer(Modifier.weight(1F))

        RedditButton()

        Spacer(Modifier.width(12.dp))

        DiscordButton()

        Spacer(Modifier.weight(1F))
    }
}