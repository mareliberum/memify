package com.codekotliners.memify.features.settings.presentation.model

sealed interface SettingsNavigation {
    data object OpenLogin : SettingsNavigation

    data object AccountChanged : SettingsNavigation
}
