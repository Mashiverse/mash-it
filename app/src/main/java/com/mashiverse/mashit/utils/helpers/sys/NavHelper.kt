package com.mashiverse.mashit.utils.helpers.sys

import androidx.navigation.NavBackStackEntry

fun NavBackStackEntry?.getCurrentTabName() = this?.destination?.route
    ?.substringAfterLast(".")
    ?.substringBefore("/")
    ?.lowercase()
    ?: ""