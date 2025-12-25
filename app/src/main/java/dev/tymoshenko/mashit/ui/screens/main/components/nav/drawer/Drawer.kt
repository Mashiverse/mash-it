package dev.tymoshenko.mashit.ui.screens.main.components.nav.drawer

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import dev.tymoshenko.mashit.ui.theme.Background

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun NavDrawer(modifier: Modifier = Modifier, navController: NavHostController) {
    val config = LocalConfiguration.current

    DismissibleDrawerSheet(
        drawerContainerColor = Background,
        drawerShape = RectangleShape,
        modifier = modifier.width((config.screenWidthDp * 0.80).dp),
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CreatePostButton(modifier = Modifier.padding(horizontal = 12.dp))

            HorizontalDivider()

            DrawerNav(
                modifier = Modifier.padding(horizontal = 12.dp),
                navController = navController
            )

            HorizontalDivider()

            PinnedArtists(modifier = Modifier.padding(horizontal = 12.dp))

            HorizontalDivider()

            OtherSection(modifier = Modifier.padding(horizontal = 12.dp))
        }
    }
}