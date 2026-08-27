package com.codekotliners.memify.features.settings.presentation.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codekotliners.memify.features.settings.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsTopBar(
    title: String,
    onBackClick: () -> Unit,
) {
    CenterAlignedTopAppBar(
        windowInsets = WindowInsets(0),
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.go_back),
                )
            }
        },
        expandedHeight = 48.dp,
    )
}
