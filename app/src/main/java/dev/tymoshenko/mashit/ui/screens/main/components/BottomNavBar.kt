package dev.tymoshenko.mashit.ui.screens.main.components

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.navigation.NavHostController
import dev.tymoshenko.mashit.data.models.navItems

@Composable
fun BottomNavBar(navController: NavHostController) {
    NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
        navItems.forEach { navItem ->
            NavigationBarItem(
                onClick = { navController.navigate(route = navItem.route) },
                label = { Text(navItem.label) },
                selected = false,
                icon = {}
            )
        }
    }
}