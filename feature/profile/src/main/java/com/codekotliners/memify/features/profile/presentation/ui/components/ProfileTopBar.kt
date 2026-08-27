package com.codekotliners.memify.features.profile.presentation.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codekotliners.memify.features.profile.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProfileTopBar(
    enabled: Boolean = true,
    onSettingsClick: () -> Unit,
) {
    CenterAlignedTopAppBar(
        windowInsets = WindowInsets(0),
        title = {
            Text(
                text = stringResource(R.string.profile),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
        },
        actions = {
            IconButton(
                enabled = enabled,
                onClick = onSettingsClick,
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = stringResource(R.string.profile_settings),
                )
            }
        },
        expandedHeight = 48.dp,
    )
}

@Composable
internal fun ProfileFloatingActionButton(
    visible: Boolean,
    onClick: () -> Unit,
) {
    if (visible) {
        FloatingActionButton(
            onClick = onClick,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = stringResource(R.string.up),
            )
        }
    }
}
