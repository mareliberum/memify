package com.codekotliners.memify.features.settings.data.datasource

import com.codekotliners.memify.core.common.Response
import com.codekotliners.memify.core.network.api.ApiConfig
import com.codekotliners.memify.core.network.api.authorizedRequest
import com.codekotliners.memify.core.network.models.RefreshRequestDto
import com.codekotliners.memify.core.network.models.UserDto
import com.codekotliners.memify.core.prefs.TokenStore
import com.codekotliners.memify.core.repositories.user.UserRepository
import com.codekotliners.memify.features.settings.data.model.SettingsAccountData
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import javax.inject.Inject

internal class SettingsRemoteDataSource @Inject constructor(
    private val httpClient: HttpClient,
    private val tokenStore: TokenStore,
    private val userRepository: UserRepository,
) {
    suspend fun getAccount(): SettingsAccountData {
        val user: UserDto =
            httpClient.authorizedRequest(tokenStore) {
                method = HttpMethod.Get
                url(ApiConfig.baseUrl + "users/me")
            }
        return SettingsAccountData(
            displayName = user.username,
            avatarUrl = user.photoUrl,
        )
    }

    suspend fun updateDisplayName(displayName: String) {
        when (val response = userRepository.updateUsername(displayName)) {
            is Response.Success -> {
                if (!response.data) {
                    error("Profile name update was rejected")
                }
            }

            is Response.Failure -> throw response.error
            Response.Loading -> error("Unexpected loading state")
        }
    }

    suspend fun revokeSession() {
        val refreshToken = tokenStore.getRefreshToken() ?: return
        httpClient.authorizedRequest<Unit>(tokenStore) {
            method = HttpMethod.Post
            url(ApiConfig.baseUrl + "auth/logout")
            contentType(ContentType.Application.Json)
            setBody(RefreshRequestDto(refreshToken))
        }
    }
}
