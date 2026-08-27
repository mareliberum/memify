package com.codekotliners.memify.features.settings.presentation.model

sealed interface SettingsThemeUiModel {
    data object System : SettingsThemeUiModel

    data object Light : SettingsThemeUiModel

    data object Dark : SettingsThemeUiModel
}
