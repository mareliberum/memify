package com.codekotliners.memify.features.settings.presentation.model

sealed interface SettingsAccountUiModel {
    data object Loading : SettingsAccountUiModel

    data object Guest : SettingsAccountUiModel

    data class Authenticated(
        val displayName: String,
        val avatarUrl: String?,
    ) : SettingsAccountUiModel
}
