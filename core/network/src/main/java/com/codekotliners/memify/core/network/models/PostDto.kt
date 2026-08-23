package com.codekotliners.memify.core.network.models

import kotlinx.serialization.Serializable

// Соответствует PostDto/CreatePostRequest в backend/src/main/kotlin/routes/PostsRoutes.kt.
// Раньше (Firestore) тут были creatorId + liked: List<String> — теперь authorId +
// likesCount/isLiked, посчитанные на бэке через таблицу post_likes.

@Serializable
data class PostDto(
    val id: String,
    val authorId: String,
    val imageUrl: String,
    val templateId: String?,
    val width: Int,
    val height: Int,
    val likesCount: Int,
    val isLiked: Boolean,
)

@Serializable
data class CreatePostRequestDto(
    val imageUrl: String,
    val templateId: String? = null,
    val width: Int,
    val height: Int,
)

@Serializable
data class ToggleLikeResponseDto(val isLiked: Boolean, val likesCount: Int)
