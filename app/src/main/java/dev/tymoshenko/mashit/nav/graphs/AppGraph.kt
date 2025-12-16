package dev.tymoshenko.mashit.nav.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import dev.tymoshenko.mashit.nav.routes.AppRoutes
import dev.tymoshenko.mashit.ui.screens.auth.Auth
import dev.tymoshenko.mashit.ui.screens.main.Main

fun NavGraphBuilder.appGraph(navController: NavHostController) {
    composable<AppRoutes.Auth> {
        Auth(onConnect = { navController.onConnect() })
    }

    composable<AppRoutes.Main> {
        Main(onDisconnect = { navController.onDisconnect() })
    }
}

fun NavHostController.onConnect() = this.navigate(AppRoutes.Main) {
    popUpTo<AppRoutes.Auth> {
        inclusive = true
    }
}

fun NavHostController.onDisconnect() = this.navigate(AppRoutes.Auth) {
    popUpTo<AppRoutes.Main> {
        inclusive = true
    }
}