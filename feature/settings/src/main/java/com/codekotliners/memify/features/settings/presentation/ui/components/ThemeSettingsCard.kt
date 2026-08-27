package com.codekotliners.memify.features.settings.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codekotliners.memify.features.settings.R
import com.codekotliners.memify.features.settings.presentation.model.SettingsThemeUiModel

@Composable
internal fun ThemeSettingsCard(
    selectedTheme: SettingsThemeUiModel,
    onThemeSelected: (SettingsThemeUiModel) -> Unit,
) {
    val themes =
        listOf(
            SettingsThemeUiModel.System,
            SettingsThemeUiModel.Light,
            SettingsThemeUiModel.Dark,
        )

    SettingsSectionCard(
        title = stringResource(R.string.appearance_title),
        description = stringResource(R.string.appearance_description),
    ) {
        Column(
            modifier = Modifier.selectableGroup(),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            themes.forEach { theme ->
                ThemeOption(
                    theme = theme,
                    selected = theme == selectedTheme,
                    onClick = { onThemeSelected(theme) },
                )
            }
        }
    }
}

@Composable
private fun ThemeOption(
    theme: SettingsThemeUiModel,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(
                    color =
                        if (selected) {
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.10f)
                        } else {
                            MaterialTheme.colorScheme.surface
                        },
                    shape = RoundedCornerShape(14.dp),
                ).selectable(
                    selected = selected,
                    onClick = onClick,
                    role = Role.RadioButton,
                ).padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = theme.title(),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            )
            Text(
                text = theme.description(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.64f),
            )
        }
        RadioButton(
            selected = selected,
            onClick = null,
        )
    }
}

@Composable
private fun SettingsThemeUiModel.title(): String =
    stringResource(
        when (this) {
            SettingsThemeUiModel.System -> R.string.theme_system
            SettingsThemeUiModel.Light -> R.string.theme_light
            SettingsThemeUiModel.Dark -> R.string.theme_dark
        },
    )

@Composable
private fun SettingsThemeUiModel.description(): String =
    stringResource(
        when (this) {
            SettingsThemeUiModel.System -> R.string.theme_system_description
            SettingsThemeUiModel.Light -> R.string.theme_light_description
            SettingsThemeUiModel.Dark -> R.string.theme_dark_description
        },
    )
