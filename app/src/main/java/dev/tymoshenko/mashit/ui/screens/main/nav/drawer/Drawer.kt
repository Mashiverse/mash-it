package dev.tymoshenko.mashit.ui.screens.main.nav.drawer

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
import dev.tymoshenko.mashit.ui.theme.DrawerPaddingSize
import dev.tymoshenko.mashit.ui.theme.PaddingSize

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun NavDrawer(modifier: Modifier = Modifier, navController: NavHostController) {
    val config = LocalConfiguration.current

    DismissibleDrawerSheet(
        drawerContainerColor = Background,
        drawerShape = RectangleShape,
        modifier = modifier.width((config.screenWidthDp * 0.80).dp),
    ) {
        Spacer(modifier = Modifier.height(PaddingSize))

        Column(
            verticalArrangement = Arrangement.spacedBy(PaddingSize)
        ) {
            CreatePostButton(modifier = Modifier.padding(horizontal = DrawerPaddingSize))

            HorizontalDivider()

            DrawerNav(
                modifier = Modifier.padding(horizontal = DrawerPaddingSize),
                navController = navController
            )

            HorizontalDivider()

            PinnedArtists(modifier = Modifier.padding(horizontal = DrawerPaddingSize))

            HorizontalDivider()

            OtherSection(modifier = Modifier.padding(horizontal = DrawerPaddingSize))
        }
    }
}