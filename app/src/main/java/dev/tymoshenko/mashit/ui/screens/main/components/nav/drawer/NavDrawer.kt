package dev.tymoshenko.mashit.ui.screens.main.components.nav.drawer

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import dev.tymoshenko.mashit.ui.theme.Background

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun NavDrawer(modifier: Modifier = Modifier) {
    val config = LocalConfiguration.current

    DismissibleDrawerSheet(
        drawerContainerColor = Background,
        drawerShape = RectangleShape,
        modifier = modifier.width((config.screenWidthDp * 0.85).dp),
    ) {
        Spacer(modifier = Modifier.height(6.dp))

        CreatePostButton(modifier = Modifier.padding(12.dp))

        Spacer(modifier = Modifier.height(6.dp))

        HorizontalDivider()
        Text("Drawer title", modifier = Modifier.padding(16.dp), color = Color.White)

        NavigationDrawerItem(
            label = { Text(text = "Drawer Item") },
            selected = false,
            onClick = { /*TODO*/ }
        )
    }
}