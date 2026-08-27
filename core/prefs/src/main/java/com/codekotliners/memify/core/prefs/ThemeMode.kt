package com.codekotliners.memify.core.prefs

enum class ThemeMode {
    FOLLOW_SYSTEM,
    LIGHT_MODE,
    DARK_MODE,
    ;

    companion object {
        fun fromStoredValue(value: String?): ThemeMode =
            entries.firstOrNull { mode -> mode.name.equals(value, ignoreCase = true) }
                ?: FOLLOW_SYSTEM
    }
}
