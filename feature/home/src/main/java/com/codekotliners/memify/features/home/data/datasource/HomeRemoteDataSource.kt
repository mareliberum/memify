package com.codekotliners.memify.features.home.data.datasource

import com.codekotliners.memify.core.common.Response
import com.codekotliners.memify.core.network.postsdatasource.PostsDatasource
import com.codekotliners.memify.core.repositories.likes.LikesRepository
import com.codekotliners.memify.core.repositories.user.UserRepository
import com.codekotliners.memify.features.home.data.mapper.toHomeData
import com.codekotliners.memify.features.home.data.model.HomeAuthorData
import com.codekotliners.memify.features.home.data.model.HomeLikeData
import com.codekotliners.memify.features.home.data.model.HomePostData
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

internal class HomeRemoteDataSource @Inject constructor(
    private val postsDatasource: PostsDatasource,
    private val userRepository: UserRepository,
    private val likesRepository: LikesRepository,
) {
    suspend fun loadPosts(): List<HomePostData> {
        val posts = postsDatasource.getPosts()
        val authors =
            loadAuthors(
                posts
                    .map { post -> post.authorId }
                    .filter { authorId -> authorId.isNotBlank() }
                    .toSet(),
            )

        return posts.map { post ->
            post.toHomeData(authors[post.authorId] ?: HomeAuthorData())
        }
    }

    suspend fun toggleLike(postId: String): HomeLikeData =
        likesRepository.toggleLike(postId).toHomeData(postId)

    private suspend fun loadAuthors(authorIds: Set<String>): Map<String, HomeAuthorData> =
        coroutineScope {
            authorIds
                .associateWith { authorId ->
                    async { loadAuthor(authorId) }
                }.mapValues { (_, author) -> author.await() }
        }

    private suspend fun loadAuthor(authorId: String): HomeAuthorData =
        try {
            when (val response = userRepository.getUserDataByUid(authorId)) {
                is Response.Success ->
                    HomeAuthorData(
                        displayName =
                            response.data[USERNAME_KEY]
                                ?.toString()
                                ?.takeIf { name -> name.isNotBlank() },
                        avatarUrl =
                            response.data[PHOTO_URL_KEY]
                                ?.toString()
                                ?.takeIf { url -> url.isNotBlank() },
                    )

                is Response.Failure, Response.Loading -> HomeAuthorData()
            }
        } catch (exception: CancellationException) {
            throw exception
        } catch (_: Exception) {
            HomeAuthorData()
        }

    private companion object {
        const val USERNAME_KEY = "username"
        const val PHOTO_URL_KEY = "photoUrl"
    }
}
