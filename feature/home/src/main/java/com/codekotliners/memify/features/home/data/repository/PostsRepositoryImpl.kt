package com.codekotliners.memify.features.home.data.repository

import com.codekotliners.memify.core.common.Response
import com.codekotliners.memify.core.mappers.toPost
import com.codekotliners.memify.core.models.Post
import com.codekotliners.memify.core.models.User
import com.codekotliners.memify.core.network.postsdatasource.PostsDatasource
import com.codekotliners.memify.core.repositories.user.UserRepository
import com.codekotliners.memify.features.home.domain.repository.PostsRepository
import com.codekotliners.memify.features.home.mocks.mockUser
import javax.inject.Inject

class PostsRepositoryImpl @Inject constructor(
    private val remoteDatasource: PostsDatasource,
    private val userRepository: UserRepository,
) : PostsRepository {
    override suspend fun getPosts(): List<Post> {
        val postDtos = remoteDatasource.getPosts()

        return postDtos.map { dto ->
            val user =
                when (val userData = userRepository.getUserDataByUid(dto.authorId)) {
                    is Response.Success -> {
                        User(
                            uid = dto.authorId,
                            profileImageUrl = userData.data["photoUrl"]?.toString() ?: "",
                            username = userData.data["username"]?.toString() ?: "",
                        )
                    }

                    is Response.Failure -> mockUser
                    Response.Loading -> mockUser
                }

            // isLiked/likesCount уже посчитаны бэком (см. PostDto), отдельный поход за
            // текущим пользователем (как раньше через FirebaseAuth) больше не нужен.
            dto.toPost(user)
        }
    }
}
