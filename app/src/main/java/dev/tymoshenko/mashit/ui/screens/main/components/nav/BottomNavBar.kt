package dev.tymoshenko.mashit.ui.screens.main.components.nav

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import dev.tymoshenko.mashit.data.models.navItems

@Composable
fun BottomNavBar(navController: NavHostController) {
    var selectedDest by rememberSaveable {
        mutableIntStateOf(0)
    }

    NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
        navItems.forEachIndexed { i, navItem ->
            NavigationBarItem(
                onClick = {
                    navController.navigate(route = navItem.route)
                    selectedDest = i
                },
                selected = i == selectedDest,
                icon = { Text(navItem.label) }
            )
        }
    }
}