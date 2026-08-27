package com.codekotliners.memify.features.settings.presentation.model

import com.codekotliners.memify.features.settings.domain.model.SettingsAccount
import com.codekotliners.memify.features.settings.domain.model.SettingsTheme

internal fun SettingsAccount.toUiModel(): SettingsAccountUiModel =
    when (this) {
        SettingsAccount.Guest -> SettingsAccountUiModel.Guest
        is SettingsAccount.Authenticated ->
            SettingsAccountUiModel.Authenticated(
                displayName = displayName,
                avatarUrl = avatarUrl,
            )
    }

internal fun SettingsTheme.toUiModel(): SettingsThemeUiModel =
    when (this) {
        SettingsTheme.System -> SettingsThemeUiModel.System
        SettingsTheme.Light -> SettingsThemeUiModel.Light
        SettingsTheme.Dark -> SettingsThemeUiModel.Dark
    }

internal fun SettingsThemeUiModel.toDomain(): SettingsTheme =
    when (this) {
        SettingsThemeUiModel.System -> SettingsTheme.System
        SettingsThemeUiModel.Light -> SettingsTheme.Light
        SettingsThemeUiModel.Dark -> SettingsTheme.Dark
    }
