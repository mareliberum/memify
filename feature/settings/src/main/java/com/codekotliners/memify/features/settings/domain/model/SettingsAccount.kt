package com.codekotliners.memify.features.settings.domain.model

sealed interface SettingsAccount {
    data object Guest : SettingsAccount

    data class Authenticated(
        val displayName: String,
        val avatarUrl: String?,
    ) : SettingsAccount
}
