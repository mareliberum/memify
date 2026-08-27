package com.codekotliners.memify.features.settings.presentation.model

data class AboutAppUiState(
    val appName: String = "",
    val packageName: String = "",
    val versionName: String = "",
    val buildNumber: Long? = null,
)
