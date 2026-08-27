package com.codekotliners.memify.features.profile.presentation.model

import com.codekotliners.memify.features.profile.domain.model.ProfileAccount
import com.codekotliners.memify.features.profile.domain.model.ProfileMeme

internal fun ProfileAccount.toUiModel(): ProfileAccountUiModel =
    when (this) {
        ProfileAccount.Guest -> ProfileAccountUiModel.Guest
        is ProfileAccount.Authenticated ->
            ProfileAccountUiModel.Authenticated(
                displayName = displayName,
                avatarUrl = avatarUrl,
            )
    }

internal fun ProfileMeme.toUiModel(): ProfileMemeUiModel =
    ProfileMemeUiModel(
        id = id,
        imageUrl = imageUrl,
        aspectRatio = width.toFloat() / height.toFloat(),
    )
