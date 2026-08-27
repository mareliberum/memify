package com.codekotliners.memify.features.home.data.repository

import com.codekotliners.memify.core.network.api.ApiException
import com.codekotliners.memify.features.home.data.datasource.HomeLocalDataSource
import com.codekotliners.memify.features.home.data.datasource.HomeRemoteDataSource
import com.codekotliners.memify.features.home.data.mapper.toDomain
import com.codekotliners.memify.features.home.domain.model.HomeFeedResult
import com.codekotliners.memify.features.home.domain.model.ToggleHomePostLikeResult
import com.codekotliners.memify.features.home.domain.repository.HomeRepository
import kotlinx.coroutines.CancellationException
import java.io.IOException
import javax.inject.Inject

internal class DefaultHomeRepository @Inject constructor(
    private val localDataSource: HomeLocalDataSource,
    private val remoteDataSource: HomeRemoteDataSource,
) : HomeRepository {
    override suspend fun loadFeed(): HomeFeedResult =
        try {
            HomeFeedResult.Success(
                posts = remoteDataSource.loadPosts().map { post -> post.toDomain() },
            )
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            when (exception) {
                is IOException, is ApiException -> HomeFeedResult.NetworkFailure
                else -> HomeFeedResult.UnknownFailure
            }
        }

    override fun isLoggedIn(): Boolean = localDataSource.isLoggedIn()

    override suspend fun toggleLike(postId: String): ToggleHomePostLikeResult =
        try {
            ToggleHomePostLikeResult.Updated(remoteDataSource.toggleLike(postId).toDomain())
        } catch (exception: ApiException) {
            if (exception.statusCode == UNAUTHORIZED_STATUS_CODE) {
                ToggleHomePostLikeResult.AuthenticationRequired
            } else {
                throw exception
            }
        }

    private companion object {
        const val UNAUTHORIZED_STATUS_CODE = 401
    }
}
