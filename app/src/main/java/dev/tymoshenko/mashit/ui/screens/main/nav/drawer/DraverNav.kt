package dev.tymoshenko.mashit.ui.screens.main.nav.drawer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import dev.tymoshenko.mashit.data.models.navItems
import dev.tymoshenko.mashit.ui.theme.Background
import dev.tymoshenko.mashit.ui.theme.ContainerColor
import dev.tymoshenko.mashit.ui.theme.ContentColor
import dev.tymoshenko.mashit.ui.theme.ContentContainerHeight
import dev.tymoshenko.mashit.ui.theme.ContentContainerShape
import dev.tymoshenko.mashit.ui.theme.PaddingSize


@Composable
fun DrawerNav(modifier: Modifier = Modifier, navController: NavHostController) {
    var selectedDest by rememberSaveable {
        mutableIntStateOf(0)
    }

    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(PaddingSize),
    ) {
        navItems.forEachIndexed { i, navItem ->
            NavigationDrawerItem(
                modifier = Modifier
                    .height(ContentContainerHeight),
                onClick = {
                    navController.navigate(route = navItem.route)
                    selectedDest = i
                },
                selected = i == selectedDest,
                label = {
                    Text(navItem.label)
                },
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = ContainerColor,
                    unselectedContainerColor = Background,
                    selectedTextColor = ContentColor,
                    unselectedTextColor = ContentColor
                ),
                shape = ContentContainerShape
            )
        }
    }
}