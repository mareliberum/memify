package com.codekotliners.memify.features.settings.domain.model

sealed interface SettingsTheme {
    data object System : SettingsTheme

    data object Light : SettingsTheme

    data object Dark : SettingsTheme
}
