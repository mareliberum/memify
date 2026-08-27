package com.codekotliners.memify.features.home.presentation.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codekotliners.memify.features.home.R
import com.codekotliners.memify.features.home.presentation.model.HomeErrorUiModel

@Composable
internal fun ErrorScreen(
    errorType: HomeErrorUiModel,
    onRetry: () -> Unit,
) {
    val title =
        when (errorType) {
            HomeErrorUiModel.Network -> stringResource(R.string.network_error_title)
            HomeErrorUiModel.Unknown -> stringResource(R.string.unknown_error_title)
        }
    val description =
        when (errorType) {
            HomeErrorUiModel.Network -> stringResource(R.string.network_error_message)
            HomeErrorUiModel.Unknown -> stringResource(R.string.unknown_error_message)
        }

    FeedStateCard(
        illustrationResId = R.drawable.round_error_outline_24,
        title = title,
        description = description,
        action = {
            Button(
                onClick = onRetry,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                    ),
                contentPadding = PaddingValues(horizontal = 20.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = stringResource(R.string.retry_feed),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        },
    )
}
