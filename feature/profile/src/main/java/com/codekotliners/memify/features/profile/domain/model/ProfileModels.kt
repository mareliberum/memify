package com.codekotliners.memify.features.profile.domain.model

sealed interface ProfileAccount {
    data object Guest : ProfileAccount

    data class Authenticated(
        val displayName: String,
        val avatarUrl: String?,
    ) : ProfileAccount
}

data class ProfileMeme(
    val id: String,
    val imageUrl: String,
    val width: Int,
    val height: Int,
)

data class ProfileSnapshot(
    val account: ProfileAccount,
    val likedMemes: List<ProfileMeme>,
    val likedMemesLoadFailed: Boolean = false,
)
