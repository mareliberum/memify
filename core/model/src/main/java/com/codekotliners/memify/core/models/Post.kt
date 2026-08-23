package com.codekotliners.memify.core.models

data class Post(
    val id: String,
    val imageUrl: String,
    val authorId: String,
    val likesCount: Int,
    val templateId: String?,
    val height: Int,
    val width: Int,
    val isLiked: Boolean,
    val author: User,
)
