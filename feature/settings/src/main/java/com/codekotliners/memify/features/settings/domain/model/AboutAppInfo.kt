package com.codekotliners.memify.features.settings.domain.model

data class AboutAppInfo(
    val appName: String,
    val packageName: String,
    val versionName: String,
    val buildNumber: Long?,
)
