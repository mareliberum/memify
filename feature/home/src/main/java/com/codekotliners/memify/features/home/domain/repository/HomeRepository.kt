package com.codekotliners.memify.features.home.domain.repository

import com.codekotliners.memify.features.home.domain.model.HomeFeedResult
import com.codekotliners.memify.features.home.domain.model.ToggleHomePostLikeResult

interface HomeRepository {
    suspend fun loadFeed(): HomeFeedResult

    fun isLoggedIn(): Boolean

    suspend fun toggleLike(postId: String): ToggleHomePostLikeResult
}
