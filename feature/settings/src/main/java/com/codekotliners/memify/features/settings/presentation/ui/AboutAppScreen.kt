package com.codekotliners.memify.features.settings.presentation.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codekotliners.memify.core.theme.MemifyTheme
import com.codekotliners.memify.features.settings.R
import com.codekotliners.memify.features.settings.presentation.model.AboutAppUiState
import com.codekotliners.memify.features.settings.presentation.ui.components.AboutAppFeaturesCard
import com.codekotliners.memify.features.settings.presentation.ui.components.AboutAppHeroCard
import com.codekotliners.memify.features.settings.presentation.ui.components.AboutAppInformationCard
import com.codekotliners.memify.features.settings.presentation.ui.components.SettingsTopBar
import com.codekotliners.memify.features.settings.presentation.viewmodel.AboutAppViewModel

@Composable
fun AboutAppScreen(
    onBackClick: () -> Unit,
    viewModel: AboutAppViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            SettingsTopBar(
                title = stringResource(R.string.about_app_title),
                onBackClick = onBackClick,
            )
        },
    ) { innerPadding ->
        AboutAppContent(
            state = state,
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(innerPadding)
                    .navigationBarsPadding(),
        )
    }
}

@Composable
private fun AboutAppContent(
    state: AboutAppUiState,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item(key = HERO_KEY) {
            AboutAppHeroCard(state)
        }
        item(key = INFORMATION_KEY) {
            AboutAppInformationCard(state)
        }
        item(key = FEATURES_KEY) {
            AboutAppFeaturesCard()
        }
    }
}

@Preview(name = "About app light", showSystemUi = true)
@Preview(name = "About app dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
private fun AboutAppScreenPreview() {
    MemifyTheme {
        Scaffold(
            topBar = {
                SettingsTopBar(
                    title = stringResource(R.string.about_app_title),
                    onBackClick = {},
                )
            },
            contentWindowInsets = WindowInsets(0),
        ) { innerPadding ->
            AboutAppContent(
                state =
                    AboutAppUiState(
                        appName = "Memify",
                        packageName = "com.codekotliners.memify",
                        versionName = "1.0",
                        buildNumber = 1,
                    ),
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
            )
        }
    }
}

private const val HERO_KEY = "about_app_hero"
private const val INFORMATION_KEY = "about_app_information"
private const val FEATURES_KEY = "about_app_features"
