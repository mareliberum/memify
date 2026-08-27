package com.codekotliners.memify.core.navigation

import androidx.navigation.NavController
import com.codekotliners.memify.core.navigation.entities.AppRoute

fun NavController.navigateToSettings(
    isAuthenticated: Boolean,
    displayName: String,
    avatarUrl: String?,
) {
    navigate(
        AppRoute.Settings(
            isAuthenticated = isAuthenticated,
            displayName = displayName,
            avatarUrl = avatarUrl,
        ),
    )
}
