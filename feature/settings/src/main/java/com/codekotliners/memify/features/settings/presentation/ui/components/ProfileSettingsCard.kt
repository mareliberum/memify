package com.codekotliners.memify.features.settings.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.codekotliners.memify.features.settings.R
import com.codekotliners.memify.features.settings.presentation.model.SettingsNameError
import com.codekotliners.memify.features.settings.presentation.model.SettingsUiState
import com.vk.id.auth.VKIDAuthUiParams
import com.vk.id.onetap.compose.onetap.OneTap
import com.vk.id.onetap.compose.onetap.OneTapTitleScenario

@Composable
internal fun ProfileSettingsCard(
    state: SettingsUiState,
    onNameChanged: (String) -> Unit,
    onSaveName: () -> Unit,
    onVkNameSelected: (String) -> Unit,
) {
    SettingsSectionCard(
        title = stringResource(R.string.profile_settings_title),
        description = stringResource(R.string.profile_settings_description),
    ) {
        OutlinedTextField(
            value = state.nameInput,
            onValueChange = onNameChanged,
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isNameSaving,
            singleLine = true,
            label = { Text(stringResource(R.string.display_name_label)) },
            isError = state.nameError != null,
            supportingText = {
                if (state.nameError == SettingsNameError.Empty) {
                    Text(stringResource(R.string.display_name_empty))
                }
            },
            keyboardOptions =
                KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Done,
                ),
            keyboardActions = KeyboardActions(onDone = { onSaveName() }),
            shape = RoundedCornerShape(14.dp),
        )

        Button(
            onClick = onSaveName,
            modifier = Modifier.fillMaxWidth(),
            enabled = state.canSaveName,
            shape = RoundedCornerShape(12.dp),
        ) {
            if (state.isNameSaving) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp,
                )
            } else {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.save_name),
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.10f))

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = stringResource(R.string.vk_name_title),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = stringResource(R.string.vk_name_description),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.64f),
            )
        }

        OneTap(
            onAuth = { _, token -> onVkNameSelected(token.userData.firstName) },
            scenario = OneTapTitleScenario.SignIn,
            authParams = VKIDAuthUiParams { scopes = setOf("photos") },
        )
    }
}
