package com.codekotliners.memify.features.settings.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codekotliners.memify.core.ui.components.AccountIdentityHeader
import com.codekotliners.memify.features.settings.R
import com.codekotliners.memify.features.settings.presentation.model.SettingsAccountUiModel
import com.codekotliners.memify.features.settings.presentation.model.SettingsUiState

@Composable
internal fun SettingsSummaryCard(
    state: SettingsUiState,
    onLoginClick: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val title = state.summaryTitle()
                AccountIdentityHeader(
                    title = title,
                    subtitle = state.summarySubtitle(),
                    avatarUrl = state.avatarUrl,
                    avatarContentDescription = title,
                )

                if (state.account is SettingsAccountUiModel.Guest) {
                    Spacer(modifier = Modifier.height(18.dp))
                    Button(
                        onClick = onLoginClick,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                            ),
                        contentPadding = PaddingValues(vertical = 12.dp),
                    ) {
                        Text(
                            text = stringResource(R.string.login),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
            }

            if (state.isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
private fun SettingsUiState.summaryTitle(): String =
    when (val account = account) {
        SettingsAccountUiModel.Loading -> ""
        SettingsAccountUiModel.Guest -> stringResource(R.string.guest_settings_title)
        is SettingsAccountUiModel.Authenticated ->
            account.displayName.ifBlank { stringResource(R.string.account_settings_title) }
    }

@Composable
private fun SettingsUiState.summarySubtitle(): String =
    stringResource(
        when (account) {
            SettingsAccountUiModel.Loading -> R.string.settings_loading_subtitle
            SettingsAccountUiModel.Guest -> R.string.guest_settings_subtitle
            is SettingsAccountUiModel.Authenticated -> R.string.account_settings_subtitle
        },
    )
