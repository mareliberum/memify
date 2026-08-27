package com.codekotliners.memify.core.prefs

import kotlinx.coroutines.flow.StateFlow

interface ThemePreferences {
    val themeMode: StateFlow<ThemeMode>

    fun setThemeMode(themeMode: ThemeMode)
}
