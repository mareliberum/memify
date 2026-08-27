package com.codekotliners.memify.core.repositories.likes

import com.codekotliners.memify.core.network.api.ApiConfig
import com.codekotliners.memify.core.network.api.authorizedRequest
import com.codekotliners.memify.core.network.models.PostDto
import com.codekotliners.memify.core.network.models.ToggleLikeResponseDto
import com.codekotliners.memify.core.prefs.TokenStore
import io.ktor.client.HttpClient
import io.ktor.client.request.url
import io.ktor.http.HttpMethod
import javax.inject.Inject

class LikesRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
    private val tokenStore: TokenStore,
) : LikesRepository {
    override suspend fun toggleLike(postId: String): ToggleLikeResponseDto =
        httpClient.authorizedRequest(tokenStore) {
            method = HttpMethod.Post
            url(ApiConfig.baseUrl + "posts/$postId/toggle-like")
        }

    override suspend fun getLikedPosts(): List<PostDto> =
        httpClient.authorizedRequest(tokenStore) {
            method = HttpMethod.Get
            url(ApiConfig.baseUrl + "posts/liked")
        }
}
