package com.codekotliners.memify.features.settings.data.model

internal data class AboutAppInfoData(
    val appName: String,
    val packageName: String,
    val versionName: String,
    val buildNumber: Long?,
)
