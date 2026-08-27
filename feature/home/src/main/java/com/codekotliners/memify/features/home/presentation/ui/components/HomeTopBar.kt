package com.codekotliners.memify.features.home.presentation.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codekotliners.memify.features.home.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeTopBar() {
    CenterAlignedTopAppBar(
        windowInsets = WindowInsets(0),
        expandedHeight = 48.dp,
        title = {
            Text(
                text = stringResource(R.string.memify),
                style = MaterialTheme.typography.titleLarge,
                fontFamily = FontFamily(Font(R.font.ubunturegular)),
                fontWeight = FontWeight.Bold,
            )
        },
    )
}
