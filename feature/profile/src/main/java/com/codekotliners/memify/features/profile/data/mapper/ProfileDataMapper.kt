package com.codekotliners.memify.features.profile.data.mapper

import com.codekotliners.memify.features.profile.data.model.ProfileAccountData
import com.codekotliners.memify.features.profile.data.model.ProfileMemeData
import com.codekotliners.memify.features.profile.domain.model.ProfileAccount
import com.codekotliners.memify.features.profile.domain.model.ProfileMeme

internal fun ProfileAccountData.toDomain(): ProfileAccount.Authenticated =
    ProfileAccount.Authenticated(
        displayName = displayName,
        avatarUrl = avatarUrl,
    )

internal fun ProfileMemeData.toDomain(): ProfileMeme =
    ProfileMeme(
        id = id,
        imageUrl = imageUrl,
        width = width.coerceAtLeast(1),
        height = height.coerceAtLeast(1),
    )
