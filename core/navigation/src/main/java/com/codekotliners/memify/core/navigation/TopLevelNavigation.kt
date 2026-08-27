package com.codekotliners.memify.core.navigation

import androidx.navigation.NavController
import com.codekotliners.memify.core.navigation.entities.AppRoute
import com.codekotliners.memify.core.navigation.entities.TopLevelDestination

fun NavController.navigateToTopLevelDestination(destination: TopLevelDestination) {
    navigate(destination.route) {
        popUpTo<AppRoute.Home> { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
