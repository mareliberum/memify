package com.codekotliners.memify.core.network.api

import com.codekotliners.memify.core.network.models.ErrorResponseDto
import com.codekotliners.memify.core.network.models.RefreshRequestDto
import com.codekotliners.memify.core.network.models.RefreshResponseDto
import com.codekotliners.memify.core.prefs.TokenStore
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import io.ktor.http.isSuccess

/** Любой не-2xx ответ бэка превращается в это исключение. */
class ApiException(val statusCode: Int, val errorMessage: String) : Exception(errorMessage)

/**
 * Единая точка входа для похода на бэк вместо Firebase SDK, который раньше сам управлял
 * сессией. Подставляет `Authorization: Bearer <accessToken>` из [TokenStore], а при ответе
 * 401 один раз пытается обновить access-токен через POST /auth/refresh и повторяет запрос.
 * Бросает [ApiException] на любой не-2xx ответ (после попытки рефреша).
 *
 * Пример использования:
 * ```
 * val user: UserDto = httpClient.authorizedRequest(tokenStore) {
 *     method = HttpMethod.Get
 *     url(ApiConfig.baseUrl + "users/me")
 * }
 * ```
 */
suspend inline fun <reified T> HttpClient.authorizedRequest(
    tokenStore: TokenStore,
    crossinline block: HttpRequestBuilder.() -> Unit,
): T {
    // Локальные `fun` внутри inline-функций пока не поддерживаются компилятором Kotlin —
    // поэтому здесь лямбда-переменная, а не `fun attempt()`. Вызывается точно так же.
    val attempt: suspend () -> HttpResponse = {
        request {
            block()
            tokenStore.getAccessToken()?.let { header(HttpHeaders.Authorization, "Bearer $it") }
        }
    }

    var response = attempt()

    if (response.status.value == 401 && tokenStore.getRefreshToken() != null) {
        if (refreshAccessToken(tokenStore)) {
            response = attempt()
        }
    }

    if (!response.status.isSuccess()) {
        val message =
            try {
                response.body<ErrorResponseDto>().error
            } catch (e: Exception) {
                response.status.description
            }
        throw ApiException(response.status.value, message)
    }

    return response.body()
}

/** Обновляет access-токен через refresh-токен. Возвращает false, если обновить не удалось. */
suspend fun HttpClient.refreshAccessToken(tokenStore: TokenStore): Boolean {
    val refreshToken = tokenStore.getRefreshToken() ?: return false
    return try {
        val response =
            request {
                method = HttpMethod.Post
                url(ApiConfig.baseUrl + "auth/refresh")
                contentType(ContentType.Application.Json)
                setBody(RefreshRequestDto(refreshToken))
            }
        if (response.status.isSuccess()) {
            val body: RefreshResponseDto = response.body()
            tokenStore.saveAccessToken(body.accessToken)
            true
        } else {
            false
        }
    } catch (e: Exception) {
        false
    }
}
