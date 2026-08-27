package com.codekotliners.memify.core.repositories.user

import com.codekotliners.memify.core.common.Response
import com.codekotliners.memify.core.models.UserData
import com.codekotliners.memify.core.network.api.ApiConfig
import com.codekotliners.memify.core.network.api.authorizedRequest
import com.codekotliners.memify.core.network.models.UpdateProfileRequestDto
import com.codekotliners.memify.core.network.models.UserDto
import com.codekotliners.memify.core.prefs.TokenStore
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
    private val tokenStore: TokenStore,
) : UserRepository {
    override suspend fun createUser(userData: UserData): Response<Boolean> = updateProfile(userData)

    override suspend fun updateProfile(userData: UserData): Response<Boolean> =
        patchProfile(
            UpdateProfileRequestDto(
                username = userData.username.takeIf { it.isNotEmpty() },
                photoUrl = userData.photoUrl,
                phone = userData.phone,
                tsi = userData.newTSI,
            ),
        )

    override suspend fun updateProfilePhoto(url: String): Response<Boolean> =
        patchProfile(UpdateProfileRequestDto(photoUrl = url))

    override suspend fun updateUsername(username: String): Response<Boolean> =
        patchProfile(UpdateProfileRequestDto(username = username))

    override suspend fun updateTSI(newTSI: Int): Response<Boolean> =
        patchProfile(UpdateProfileRequestDto(tsi = newTSI))

    override suspend fun updatePassword(currentPassword: String, newPassword: String): Response<Boolean> =
        // На бэке пока нет отдельного эндпоинта "сменить пароль, будучи залогиненным" — есть
        // только восстановление по email (/auth/forgot-password + /auth/reset-password, см.
        // AuthRoutes.kt). Если нужна смена пароля прямо из настроек профиля, на бэке стоит
        // добавить, например, PATCH /users/me/password (с проверкой currentPassword).
        Response.Failure(
            UnsupportedOperationException(
                "Смена пароля из профиля пока не поддерживается бэкендом — только восстановление по email",
            ),
        )

    private suspend fun patchProfile(body: UpdateProfileRequestDto): Response<Boolean> =
        try {
            httpClient.authorizedRequest<Unit>(tokenStore) {
                method = HttpMethod.Patch
                url(ApiConfig.baseUrl + "users/me")
                contentType(ContentType.Application.Json)
                setBody(body)
            }
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }

    override suspend fun getUid(): Response<String?> = Response.Success(tokenStore.getUserId())

    override suspend fun getUserDataByUid(uid: String): Response<Map<String, Any>> =
        try {
            val user: UserDto =
                httpClient.authorizedRequest(tokenStore) {
                    method = HttpMethod.Get
                    url(ApiConfig.baseUrl + "users/$uid")
                }
            Response.Success(
                mapOf(
                    "id" to user.id,
                    "username" to user.username,
                    "email" to user.email,
                    "photoUrl" to (user.photoUrl ?: ""),
                    "phone" to (user.phone ?: ""),
                    "tsi" to user.tsi,
                ),
            )
        } catch (e: Exception) {
            Response.Failure(e)
        }
}
