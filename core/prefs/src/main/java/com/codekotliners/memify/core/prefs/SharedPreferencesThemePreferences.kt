package com.codekotliners.memify.core.prefs

import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesThemePreferences @Inject constructor(
    private val sharedPreferences: SharedPreferences,
) : ThemePreferences {
    private val _themeMode =
        MutableStateFlow(
            ThemeMode.fromStoredValue(sharedPreferences.getString(THEME_KEY, null)),
        )

    override val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    override fun setThemeMode(themeMode: ThemeMode) {
        sharedPreferences.edit().putString(THEME_KEY, themeMode.name).apply()
        _themeMode.value = themeMode
    }

    private companion object {
        const val THEME_KEY = "theme"
    }
}
