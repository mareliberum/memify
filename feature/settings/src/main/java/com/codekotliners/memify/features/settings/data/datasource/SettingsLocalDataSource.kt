package com.codekotliners.memify.features.settings.data.datasource

import com.codekotliners.memify.core.prefs.ThemeMode
import com.codekotliners.memify.core.prefs.ThemePreferences
import com.codekotliners.memify.core.prefs.TokenStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class SettingsLocalDataSource @Inject constructor(
    private val themePreferences: ThemePreferences,
    private val tokenStore: TokenStore,
) {
    fun observeTheme(): Flow<ThemeMode> = themePreferences.themeMode

    fun isAuthenticated(): Boolean = tokenStore.isLoggedIn()

    fun setTheme(themeMode: ThemeMode) {
        themePreferences.setThemeMode(themeMode)
    }

    fun clearSession() {
        tokenStore.clear()
    }
}
