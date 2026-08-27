package com.codekotliners.memify

import androidx.lifecycle.ViewModel
import com.codekotliners.memify.core.prefs.ThemePreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppThemeViewModel @Inject constructor(
    themePreferences: ThemePreferences,
) : ViewModel() {
    val themeMode = themePreferences.themeMode
}
