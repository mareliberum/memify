package com.codekotliners.memify.core.usecases

import android.net.Uri
import android.util.Log
import com.codekotliners.memify.core.common.Response
import com.codekotliners.memify.core.network.models.PostDto
import com.codekotliners.memify.core.network.postsdatasource.PostsDatasource
import com.codekotliners.memify.core.repositories.user.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PublishImageUseCase @Inject constructor(
    private val remoteDatasource: PostsDatasource,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(imageUri: Uri, height: Int, width: Int): Response<Boolean> =
        withContext(Dispatchers.IO) {
            val uid =
                when (val response = userRepository.getUid()) {
                    is Response.Failure -> ""
                    is Response.Success -> response.data
                    Response.Loading -> throw IllegalStateException("Unexpected Loading State")
                }
            // id/likesCount/isLiked тут — заглушки: реальные значения назначит бэк при
            // создании поста (POST /posts), см. PostsRestDatasource.uploadPost().
            val postDto =
                PostDto(
                    id = "id",
                    imageUrl = "",
                    authorId = uid ?: "",
                    // TODO тут научиться передавать templateid, чтобы потом можно было брать шаблон
                    templateId = "templateId",
                    height = height,
                    width = width,
                    likesCount = 0,
                    isLiked = false,
                )
            if (remoteDatasource.uploadPost(postDto, imageUri)) {
                Log.d("test", "post uploaded successfully")
                Response.Success(true)
            } else {
                Response.Failure(IllegalStateException("Failure Uploading post"))
            }
        }
}
