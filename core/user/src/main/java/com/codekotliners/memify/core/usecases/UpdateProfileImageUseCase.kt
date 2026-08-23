package com.codekotliners.memify.core.usecases

import android.content.Context
import android.net.Uri
import android.util.Log
import com.codekotliners.memify.core.common.Response
import com.codekotliners.memify.core.network.api.ApiConfig
import com.codekotliners.memify.core.network.api.authorizedRequest
import com.codekotliners.memify.core.prefs.TokenStore
import com.codekotliners.memify.core.repositories.user.UserRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import javax.inject.Inject

class UpdateProfileImageUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val httpClient: HttpClient,
    private val tokenStore: TokenStore,
    @ApplicationContext private val context: Context,
) {
    // Раньше грузили напрямую в Firebase Storage. Теперь — POST /upload на свой бэк
    // (тот же эндпоинт, что используют посты, см. UploadRoutes.kt), а потом сохраняем
    // полученный url в профиле через PATCH /users/me.
    // Блочное тело (не "= try { ... }") — внутри нужен bare `return`, а он запрещён
    // в функциях с expression body.
    suspend operator fun invoke(imageUri: Uri): Response<String> {
        return try {
            val bytes =
                context.contentResolver.openInputStream(imageUri)?.use { it.readBytes() }
                    ?: return Response.Failure(IllegalStateException("Can't read image"))

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
                                        append(HttpHeaders.ContentDisposition, "filename=\"profile.jpg\"")
                                    },
                                )
                            },
                        ),
                    )
                }

            val downloadUrl =
                uploadResponse["url"]
                    ?: return Response.Failure(IllegalStateException("Upload failed: no url in response"))

            Log.d("test", "download url $downloadUrl")
            userRepository.updateProfilePhoto(downloadUrl)

            Response.Success(downloadUrl)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    suspend fun getProfileImageUrl(): String? {
        val photoUrl =
            when (val response = userRepository.getUserPhotoUrl()) {
                is Response.Failure -> {
                    Log.d("test", " error gering url {${response.error.message}}")
                    return null
                }
                is Response.Success -> response.data
                Response.Loading -> throw IllegalStateException("Unexpected loading state")
            }

        return photoUrl
    }
}
