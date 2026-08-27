package com.codekotliners.memify.features.settings.presentation.model

import com.codekotliners.memify.features.settings.domain.model.AboutAppInfo

internal fun AboutAppInfo.toUiState(): AboutAppUiState =
    AboutAppUiState(
        appName = appName,
        packageName = packageName,
        versionName = versionName,
        buildNumber = buildNumber,
    )
