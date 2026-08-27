package com.codekotliners.memify.core.navigation

import androidx.navigation.NavController
import com.codekotliners.memify.core.navigation.entities.NavRoutes

fun NavController.navigateToSettings(
    isAuthenticated: Boolean,
    displayName: String,
    avatarUrl: String?,
) {
    navigate(
        NavRoutes.Settings.createRoute(
            isAuthenticated = isAuthenticated,
            displayName = displayName,
            avatarUrl = avatarUrl,
        ),
    )
}
