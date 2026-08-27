package com.codekotliners.memify.features.settings.presentation.model

sealed interface SettingsAction {
    data object Refresh : SettingsAction

    data object AuthChanged : SettingsAction

    data class ThemeSelected(
        val theme: SettingsThemeUiModel,
    ) : SettingsAction

    data class NameChanged(
        val name: String,
    ) : SettingsAction

    data object SaveNameClicked : SettingsAction

    data class VkNameSelected(
        val name: String,
    ) : SettingsAction

    data object LoginClicked : SettingsAction

    data object SignOutRequested : SettingsAction

    data object SignOutDismissed : SettingsAction

    data object SignOutConfirmed : SettingsAction

    data object MessageShown : SettingsAction

    data object NavigationHandled : SettingsAction
}
