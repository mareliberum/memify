package com.codekotliners.memify.features.settings.data.mapper

import com.codekotliners.memify.features.settings.data.model.AboutAppInfoData
import com.codekotliners.memify.features.settings.domain.model.AboutAppInfo

internal fun AboutAppInfoData.toDomain(): AboutAppInfo =
    AboutAppInfo(
        appName = appName,
        packageName = packageName,
        versionName = versionName,
        buildNumber = buildNumber,
    )
