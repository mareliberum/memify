package com.codekotliners.memify.features.settings.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.codekotliners.memify.features.settings.R
import com.codekotliners.memify.features.settings.presentation.model.AboutAppUiState

@Composable
internal fun AboutAppInformationCard(state: AboutAppUiState) {
    val unknownValue = stringResource(R.string.about_app_unknown_value)

    SettingsSectionCard(
        title = stringResource(R.string.about_app_information_title),
        description = stringResource(R.string.about_app_information_description),
    ) {
        Column {
            AboutAppInformationRow(
                label = stringResource(R.string.about_app_version),
                value = state.versionName.ifBlank { unknownValue },
            )
            AboutAppInformationDivider()
            AboutAppInformationRow(
                label = stringResource(R.string.about_app_build_number),
                value = state.buildNumber?.toString() ?: unknownValue,
            )
            AboutAppInformationDivider()
            AboutAppInformationRow(
                label = stringResource(R.string.about_app_platform),
                value = stringResource(R.string.about_app_platform_android),
            )
            AboutAppInformationDivider()
            AboutAppInformationRow(
                label = stringResource(R.string.about_app_package_name),
                value = state.packageName.ifBlank { unknownValue },
            )
        }
    }
}

@Composable
private fun AboutAppInformationRow(
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.64f),
        )
        Text(
            text = value,
            modifier = Modifier.weight(1.25f),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.End,
        )
    }
}

@Composable
private fun AboutAppInformationDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 12.dp),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
    )
}
