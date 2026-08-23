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

/**
 * Раньше лайки были массивом userId прямо в документе поста в Firestore. Теперь на бэке
 * это отдельная таблица post_likes, а PostDto уже приходит с готовыми likesCount/isLiked —
 * поэтому isLiked/likesCount тут просто читают поля из PostDto.
 */
class LikesRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
    private val tokenStore: TokenStore,
) : LikesRepository {
    override suspend fun likeTap(postsDto: PostDto) {
        httpClient.authorizedRequest<ToggleLikeResponseDto>(tokenStore) {
            method = HttpMethod.Post
            url(ApiConfig.baseUrl + "posts/${postsDto.id}/toggle-like")
        }
    }

    override suspend fun isLiked(postsDto: PostDto): Boolean = postsDto.isLiked

    override suspend fun likesCount(postsDto: PostDto): Int = postsDto.likesCount

    override suspend fun getLikedPosts(): List<PostDto> =
        httpClient.authorizedRequest(tokenStore) {
            method = HttpMethod.Get
            url(ApiConfig.baseUrl + "posts/liked")
        }
}
