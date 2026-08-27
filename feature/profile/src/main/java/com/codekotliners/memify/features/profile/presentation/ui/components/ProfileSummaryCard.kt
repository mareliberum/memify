package com.codekotliners.memify.features.profile.presentation.ui.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codekotliners.memify.core.ui.components.AccountIdentityHeader
import com.codekotliners.memify.features.profile.R
import com.codekotliners.memify.features.profile.presentation.model.ProfileAccountUiModel
import com.codekotliners.memify.features.profile.presentation.model.ProfileUiState

@Composable
internal fun ProfileSummaryCard(
    state: ProfileUiState,
    onLoginClick: () -> Unit,
    onAvatarSelected: (String) -> Unit,
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
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                ProfileIdentityHeader(
                    state = state,
                    onAvatarSelected = onAvatarSelected,
                )

                Spacer(modifier = Modifier.height(18.dp))

                when (state.account) {
                    ProfileAccountUiModel.Loading -> Unit
                    ProfileAccountUiModel.Guest -> {
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
                                text = stringResource(R.string.log_in_account),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }

                    is ProfileAccountUiModel.Authenticated -> {
                        ProfileStats(
                            createdCount = state.createdMemes.size,
                            likedCount = state.likedMemes.size,
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
private fun ProfileIdentityHeader(
    state: ProfileUiState,
    onAvatarSelected: (String) -> Unit,
) {
    val pickMedia =
        rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
            if (uri != null) {
                onAvatarSelected(uri.toString())
            }
        }
    val selectImage: () -> Unit = {
        pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
    }
    val avatarClickable = state.isLoggedIn && !state.isAvatarUpdating
    val title = state.title()
    val subtitle =
        stringResource(
            when (state.account) {
                ProfileAccountUiModel.Loading -> R.string.profile_loading
                ProfileAccountUiModel.Guest -> R.string.guest_profile_subtitle
                is ProfileAccountUiModel.Authenticated -> R.string.logged_profile_subtitle
            },
        )

    AccountIdentityHeader(
        title = title,
        subtitle = subtitle,
        avatarUrl = state.avatarUrl,
        avatarContentDescription = title,
        onAvatarClick = selectImage.takeIf { avatarClickable },
        avatarBadge =
            if (state.isLoggedIn) {
                {
                    ProfileAvatarBadge(
                        isUpdating = state.isAvatarUpdating,
                        isClickable = avatarClickable,
                        onClick = selectImage,
                    )
                }
            } else {
                null
            },
    )
}

@Composable
private fun BoxScope.ProfileAvatarBadge(
    isUpdating: Boolean,
    isClickable: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .align(Alignment.BottomEnd)
                .size(32.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape)
                .then(
                    if (isClickable) {
                        Modifier.clickable(onClick = onClick)
                    } else {
                        Modifier
                    },
                ),
        contentAlignment = Alignment.Center,
    ) {
        if (isUpdating) {
            CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp,
            )
        } else {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = stringResource(R.string.change_profile_photo),
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}

@Composable
private fun ProfileStats(
    createdCount: Int,
    likedCount: Int,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f))
                .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ProfileStat(
            value = createdCount,
            label = stringResource(R.string.created_count_label),
            modifier = Modifier.weight(1f),
        )

        Box(
            modifier =
                Modifier
                    .width(1.dp)
                    .height(36.dp)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)),
        )

        ProfileStat(
            value = likedCount,
            label = stringResource(R.string.liked_count_label),
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun ProfileStat(
    value: Int,
    label: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.64f),
        )
    }
}

@Composable
private fun ProfileUiState.title(): String =
    when (account) {
        ProfileAccountUiModel.Loading -> stringResource(R.string.profile)
        ProfileAccountUiModel.Guest -> stringResource(R.string.guest_profile_title)
        is ProfileAccountUiModel.Authenticated -> {
            if (displayName.isBlank()) {
                stringResource(R.string.profile)
            } else {
                displayName
            }
        }
    }
