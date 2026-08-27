package com.codekotliners.memify.features.settings.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codekotliners.memify.features.settings.R

@Composable
internal fun AccountSettingsCard(
    isSigningOut: Boolean,
    onSignOutClick: () -> Unit,
) {
    SettingsSectionCard(
        title = stringResource(R.string.account_control_title),
        description = stringResource(R.string.account_control_description),
    ) {
        OutlinedButton(
            onClick = onSignOutClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isSigningOut,
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.72f)),
        ) {
            if (isSigningOut) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    color = MaterialTheme.colorScheme.error,
                    strokeWidth = 2.dp,
                )
            } else {
                Text(
                    text = stringResource(R.string.logout),
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}
