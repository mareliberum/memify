package com.codekotliners.memify.features.settings.data.mapper

import com.codekotliners.memify.core.prefs.ThemeMode
import com.codekotliners.memify.features.settings.data.model.SettingsAccountData
import com.codekotliners.memify.features.settings.domain.model.SettingsAccount
import com.codekotliners.memify.features.settings.domain.model.SettingsTheme

internal fun SettingsAccountData.toDomain(): SettingsAccount.Authenticated =
    SettingsAccount.Authenticated(
        displayName = displayName,
        avatarUrl = avatarUrl,
    )

internal fun ThemeMode.toDomain(): SettingsTheme =
    when (this) {
        ThemeMode.FOLLOW_SYSTEM -> SettingsTheme.System
        ThemeMode.LIGHT_MODE -> SettingsTheme.Light
        ThemeMode.DARK_MODE -> SettingsTheme.Dark
    }

internal fun SettingsTheme.toData(): ThemeMode =
    when (this) {
        SettingsTheme.System -> ThemeMode.FOLLOW_SYSTEM
        SettingsTheme.Light -> ThemeMode.LIGHT_MODE
        SettingsTheme.Dark -> ThemeMode.DARK_MODE
    }
