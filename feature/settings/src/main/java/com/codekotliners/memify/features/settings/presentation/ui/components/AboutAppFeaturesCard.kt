package com.codekotliners.memify.features.settings.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codekotliners.memify.features.settings.R

@Composable
internal fun AboutAppFeaturesCard() {
    SettingsSectionCard(
        title = stringResource(R.string.about_app_features_title),
        description = stringResource(R.string.about_app_features_description),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            AboutAppFeatureRow(
                icon = Icons.Default.Create,
                title = stringResource(R.string.about_app_editor_title),
                description = stringResource(R.string.about_app_editor_description),
            )
            AboutAppFeatureRow(
                icon = Icons.Default.Star,
                title = stringResource(R.string.about_app_templates_title),
                description = stringResource(R.string.about_app_templates_description),
            )
            AboutAppFeatureRow(
                icon = Icons.Default.Share,
                title = stringResource(R.string.about_app_sharing_title),
                description = stringResource(R.string.about_app_sharing_description),
            )
        }
    }
}

@Composable
private fun AboutAppFeatureRow(
    icon: ImageVector,
    title: String,
    description: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .size(42.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(21.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f),
            )
        }
    }
}
