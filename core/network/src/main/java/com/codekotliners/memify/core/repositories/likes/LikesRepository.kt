package com.codekotliners.memify.core.repositories.likes

import com.codekotliners.memify.core.network.models.PostDto
import com.codekotliners.memify.core.network.models.ToggleLikeResponseDto

interface LikesRepository {
    suspend fun toggleLike(postId: String): ToggleLikeResponseDto

    suspend fun getLikedPosts(): List<PostDto>
}
