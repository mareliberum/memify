package com.codekotliners.memify.features.settings.presentation.model

data class SettingsUiState(
    val isLoading: Boolean = true,
    val account: SettingsAccountUiModel = SettingsAccountUiModel.Loading,
    val selectedTheme: SettingsThemeUiModel = SettingsThemeUiModel.System,
    val nameInput: String = "",
    val nameError: SettingsNameError? = null,
    val isNameSaving: Boolean = false,
    val isSigningOut: Boolean = false,
    val isSignOutConfirmationVisible: Boolean = false,
    val message: SettingsMessage? = null,
    val navigation: SettingsNavigation? = null,
) {
    val isLoggedIn: Boolean
        get() = account is SettingsAccountUiModel.Authenticated

    val displayName: String
        get() = (account as? SettingsAccountUiModel.Authenticated)?.displayName.orEmpty()

    val avatarUrl: String?
        get() = (account as? SettingsAccountUiModel.Authenticated)?.avatarUrl

    val canSaveName: Boolean
        get() =
            isLoggedIn &&
                nameInput.isNotBlank() &&
                nameInput.trim() != displayName &&
                !isNameSaving
}
