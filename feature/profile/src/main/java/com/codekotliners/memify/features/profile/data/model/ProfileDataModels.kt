package com.codekotliners.memify.features.profile.data.model

internal data class ProfileAccountData(
    val displayName: String,
    val avatarUrl: String?,
)

internal data class ProfileMemeData(
    val id: String,
    val imageUrl: String,
    val width: Int,
    val height: Int,
)
