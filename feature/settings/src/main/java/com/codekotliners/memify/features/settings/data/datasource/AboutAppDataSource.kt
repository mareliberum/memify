package com.codekotliners.memify.features.settings.data.datasource

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import com.codekotliners.memify.features.settings.data.model.AboutAppInfoData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class AboutAppDataSource @Inject constructor(
    @param:ApplicationContext private val context: Context,
) {
    fun getInfo(): AboutAppInfoData {
        val packageManager = context.packageManager
        val packageName = context.packageName
        val packageInfo = runCatching { packageManager.getPackageInfo(packageName) }.getOrNull()
        val appName =
            runCatching { context.applicationInfo.loadLabel(packageManager).toString() }
                .getOrNull()
                .orEmpty()
                .ifBlank { DEFAULT_APP_NAME }

        return AboutAppInfoData(
            appName = appName,
            packageName = packageName,
            versionName = packageInfo?.versionName.orEmpty(),
            buildNumber = packageInfo?.versionCodeValue(),
        )
    }

    @Suppress("DEPRECATION")
    private fun PackageManager.getPackageInfo(packageName: String): PackageInfo =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
        } else {
            getPackageInfo(packageName, 0)
        }

    @Suppress("DEPRECATION")
    private fun PackageInfo.versionCodeValue(): Long =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            longVersionCode
        } else {
            versionCode.toLong()
        }

    private companion object {
        const val DEFAULT_APP_NAME = "Memify"
    }
}
