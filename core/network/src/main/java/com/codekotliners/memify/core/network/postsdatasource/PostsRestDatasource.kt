package com.codekotliners.memify.core.network.postsdatasource

import android.content.Context
import android.net.Uri
import com.codekotliners.memify.core.network.api.ApiConfig
import com.codekotliners.memify.core.network.api.authorizedRequest
import com.codekotliners.memify.core.network.models.CreatePostRequestDto
import com.codekotliners.memify.core.network.models.PostDto
import com.codekotliners.memify.core.prefs.TokenStore
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import javax.inject.Inject

/**
 * REST-реализация вместо старой [PostsFbStorageDatasource] (Firebase Firestore + Storage).
 * Загрузка картинки — через POST /upload (multipart), сам пост — через POST /posts.
 */
class PostsRestDatasource @Inject constructor(
    private val httpClient: HttpClient,
    private val tokenStore: TokenStore,
    @ApplicationContext private val context: Context,
) : PostsDatasource {
    override suspend fun getPostById(id: String): PostDto =
        httpClient.authorizedRequest(tokenStore) {
            method = HttpMethod.Get
            url(ApiConfig.baseUrl + "posts/$id")
        }

    override suspend fun getPosts(): List<PostDto> =
        httpClient.authorizedRequest(tokenStore) {
            method = HttpMethod.Get
            url(ApiConfig.baseUrl + "posts")
        }

    // Блочное тело (не "= try { ... }") — внутри нужен bare `return false`, а он запрещён
    // в функциях с expression body (см. ошибку компиляции "Returns are not allowed for
    // functions with expression body").
    override suspend fun uploadPost(post: PostDto, imageUri: Uri): Boolean {
        return try {
            val bytes =
                context.contentResolver.openInputStream(imageUri)?.use { it.readBytes() }
                    ?: return false

            val uploadResponse: Map<String, String?> =
                httpClient.authorizedRequest(tokenStore) {
                    method = HttpMethod.Post
                    url(ApiConfig.baseUrl + "upload")
                    setBody(
                        MultiPartFormDataContent(
                            formData {
                                append(
                                    "file",
                                    bytes,
                                    Headers.build {
                                        append(HttpHeaders.ContentType, "image/*")
                                        append(HttpHeaders.ContentDisposition, "filename=\"image.jpg\"")
                                    },
                                )
                            },
                        ),
                    )
                }
            val imageUrl = uploadResponse["url"] ?: return false

            httpClient.authorizedRequest<PostDto>(tokenStore) {
                method = HttpMethod.Post
                url(ApiConfig.baseUrl + "posts")
                contentType(ContentType.Application.Json)
                setBody(
                    CreatePostRequestDto(
                        imageUrl = imageUrl,
                        templateId = post.templateId,
                        width = post.width,
                        height = post.height,
                    ),
                )
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}
