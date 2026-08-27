package com.codekotliners.memify.features.home.data.model

internal data class HomeAuthorData(
    val displayName: String? = null,
    val avatarUrl: String? = null,
)

internal data class HomePostData(
    val id: String,
    val imageUrl: String,
    val width: Int,
    val height: Int,
    val likesCount: Int,
    val isLiked: Boolean,
    val author: HomeAuthorData,
)

internal data class HomeLikeData(
    val postId: String,
    val isLiked: Boolean,
    val likesCount: Int,
)
