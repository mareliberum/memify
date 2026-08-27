package com.codekotliners.memify.features.settings.presentation.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codekotliners.memify.core.theme.MemifyTheme
import com.codekotliners.memify.core.ui.components.AppScaffold
import com.codekotliners.memify.features.settings.R
import com.codekotliners.memify.features.settings.presentation.model.SettingsAccountUiModel
import com.codekotliners.memify.features.settings.presentation.model.SettingsAction
import com.codekotliners.memify.features.settings.presentation.model.SettingsMessage
import com.codekotliners.memify.features.settings.presentation.model.SettingsNavigation
import com.codekotliners.memify.features.settings.presentation.model.SettingsThemeUiModel
import com.codekotliners.memify.features.settings.presentation.model.SettingsUiState
import com.codekotliners.memify.features.settings.presentation.ui.components.AccountSettingsCard
import com.codekotliners.memify.features.settings.presentation.ui.components.AboutAppSettingsCard
import com.codekotliners.memify.features.settings.presentation.ui.components.ProfileSettingsCard
import com.codekotliners.memify.features.settings.presentation.ui.components.SettingsSummaryCard
import com.codekotliners.memify.features.settings.presentation.ui.components.SettingsTopBar
import com.codekotliners.memify.features.settings.presentation.ui.components.SignOutConfirmationDialog
import com.codekotliners.memify.features.settings.presentation.ui.components.ThemeSettingsCard
import com.codekotliners.memify.features.settings.presentation.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    authChanged: Boolean,
    onAuthChangedHandled: () -> Unit,
    onNavigateToAuth: () -> Unit,
    onAccountChanged: () -> Unit,
    onBackClick: () -> Unit,
    onAboutAppClick: () -> Unit,
    bottomBar: @Composable () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val messageText = state.message?.text()

    LaunchedEffect(authChanged) {
        if (authChanged) {
            onAuthChangedHandled()
            viewModel.onAction(SettingsAction.AuthChanged)
        }
    }

    LaunchedEffect(state.message, messageText) {
        if (messageText != null) {
            snackbarHostState.showSnackbar(messageText)
            viewModel.onAction(SettingsAction.MessageShown)
        }
    }

    LaunchedEffect(state.navigation) {
        when (state.navigation) {
            SettingsNavigation.OpenLogin -> onNavigateToAuth()
            SettingsNavigation.AccountChanged -> onAccountChanged()

            null -> return@LaunchedEffect
        }
        viewModel.onAction(SettingsAction.NavigationHandled)
    }

    if (state.isSignOutConfirmationVisible) {
        SignOutConfirmationDialog(
            onConfirm = { viewModel.onAction(SettingsAction.SignOutConfirmed) },
            onDismiss = { viewModel.onAction(SettingsAction.SignOutDismissed) },
        )
    }

    AppScaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SettingsTopBar(
                title = stringResource(R.string.settings_title),
                onBackClick = onBackClick,
            )
        },
        bottomBar = bottomBar,
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->
        SettingsContent(
            state = state,
            onAction = viewModel::onAction,
            onAboutAppClick = onAboutAppClick,
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
        )
    }
}

@Composable
private fun SettingsContent(
    state: SettingsUiState,
    onAction: (SettingsAction) -> Unit,
    onAboutAppClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item(key = SUMMARY_KEY) {
            SettingsSummaryCard(
                state = state,
                onLoginClick = { onAction(SettingsAction.LoginClicked) },
            )
        }

        item(key = APPEARANCE_KEY) {
            ThemeSettingsCard(
                selectedTheme = state.selectedTheme,
                onThemeSelected = { theme -> onAction(SettingsAction.ThemeSelected(theme)) },
            )
        }

        if (state.account is SettingsAccountUiModel.Authenticated) {
            item(key = PROFILE_KEY) {
                ProfileSettingsCard(
                    state = state,
                    onNameChanged = { name -> onAction(SettingsAction.NameChanged(name)) },
                    onSaveName = { onAction(SettingsAction.SaveNameClicked) },
                    onVkNameSelected = { name -> onAction(SettingsAction.VkNameSelected(name)) },
                )
            }

            item(key = ACCOUNT_KEY) {
                AccountSettingsCard(
                    isSigningOut = state.isSigningOut,
                    onSignOutClick = { onAction(SettingsAction.SignOutRequested) },
                )
            }
        }

        item(key = ABOUT_APP_KEY) {
            AboutAppSettingsCard(onClick = onAboutAppClick)
        }
    }
}

@Composable
private fun SettingsMessage.text(): String =
    stringResource(
        when (this) {
            SettingsMessage.AccountLoadFailed -> R.string.account_load_failed
            SettingsMessage.NameUpdated -> R.string.name_updated
            SettingsMessage.NameUpdateFailed -> R.string.name_update_failed
            SettingsMessage.SignedOut -> R.string.signed_out
            SettingsMessage.SignOutFailed -> R.string.sign_out_failed
            SettingsMessage.VkNameUnavailable -> R.string.vk_name_unavailable
        },
    )

@Preview(name = "Settings light", showSystemUi = true)
@Preview(name = "Settings dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
private fun SettingsScreenPreview() {
    MemifyTheme {
        Scaffold(
            topBar = {
                SettingsTopBar(
                    title = stringResource(R.string.settings_title),
                    onBackClick = {},
                )
            },
            contentWindowInsets = WindowInsets(0),
        ) { innerPadding ->
            SettingsContent(
                state =
                    SettingsUiState(
                        isLoading = false,
                        account =
                            SettingsAccountUiModel.Authenticated(
                                displayName = "MemeMaker2011",
                                avatarUrl = null,
                            ),
                        selectedTheme = SettingsThemeUiModel.System,
                        nameInput = "MemeMaker2011",
                    ),
                onAction = {},
                onAboutAppClick = {},
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
            )
        }
    }
}

private const val SUMMARY_KEY = "settings_summary"
private const val APPEARANCE_KEY = "settings_appearance"
private const val PROFILE_KEY = "settings_profile"
private const val ACCOUNT_KEY = "settings_account"
private const val ABOUT_APP_KEY = "settings_about_app"
