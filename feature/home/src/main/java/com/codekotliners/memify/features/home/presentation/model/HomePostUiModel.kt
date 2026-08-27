package com.codekotliners.memify.features.home.presentation.model

data class HomePostUiModel(
    val id: String,
    val imageUrl: String,
    val aspectRatio: Float,
    val authorName: String?,
    val authorAvatarUrl: String?,
    val likesCount: Int,
    val isLiked: Boolean,
)
