package com.codekotliners.memify.features.settings.presentation.model

sealed interface SettingsNameError {
    data object Empty : SettingsNameError
}
