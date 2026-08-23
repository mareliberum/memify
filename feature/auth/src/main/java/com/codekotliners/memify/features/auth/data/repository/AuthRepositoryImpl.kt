package com.codekotliners.memify.features.auth.data.repository

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import com.codekotliners.memify.core.common.Response
import com.codekotliners.memify.core.network.api.ApiConfig
import com.codekotliners.memify.core.network.api.authorizedRequest
import com.codekotliners.memify.core.network.models.AuthResponseDto
import com.codekotliners.memify.core.network.models.ForgotPasswordRequestDto
import com.codekotliners.memify.core.network.models.GoogleAuthRequestDto
import com.codekotliners.memify.core.network.models.LoginRequestDto
import com.codekotliners.memify.core.network.models.RefreshRequestDto
import com.codekotliners.memify.core.network.models.RegisterRequestDto
import com.codekotliners.memify.core.prefs.TokenStore
import com.codekotliners.memify.features.auth.di.GoogleWebClientId
import com.codekotliners.memify.features.auth.domain.repository.AuthRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException as GoogleApiException
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * Полностью своя авторизация вместо Firebase Auth: email/password + Google — но Google
 * ID-токен теперь проверяет собственный бэк (/auth/google), а не Firebase. GoogleSignInClient
 * тут остаётся — это чистый Google Sign-In SDK (play-services-auth), к Firebase не привязан,
 * он только выдаёт ID-токен, который дальше идёт на бэк.
 */
class AuthRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
    private val tokenStore: TokenStore,
    @ApplicationContext private val context: Context,
    @GoogleWebClientId private val webClientId: String,
) : AuthRepository {
    private val googleSignInClient by lazy {
        GoogleSignIn.getClient(
            context,
            GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(webClientId)
                .requestEmail()
                .build(),
        )
    }

    // Раньше слушали живые события FirebaseAuth.AuthStateListener. У REST-бэка такого нет —
    // тут просто фиксируем состояние на момент вызова (обычно вызывается один раз при старте).
    override fun getAuthState(viewModelScope: CoroutineScope): StateFlow<Boolean> =
        MutableStateFlow(tokenStore.getAccessToken() == null).asStateFlow()

    override suspend fun getCurrentUser(): String? = tokenStore.getUserId()

    override suspend fun firebaseCreateAccount(name: String, email: String, password: String): Response<Boolean> =
        try {
            val response: AuthResponseDto =
                httpClient.authorizedRequest(tokenStore) {
                    method = HttpMethod.Post
                    url(ApiConfig.baseUrl + "auth/register")
                    contentType(ContentType.Application.Json)
                    setBody(RegisterRequestDto(email = email, password = password, username = name))
                }
            tokenStore.saveTokens(response.accessToken, response.refreshToken, response.userId)
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }

    override suspend fun firebaseSignIn(email: String, password: String): Response<Boolean> =
        try {
            val response: AuthResponseDto =
                httpClient.authorizedRequest(tokenStore) {
                    method = HttpMethod.Post
                    url(ApiConfig.baseUrl + "auth/login")
                    contentType(ContentType.Application.Json)
                    setBody(LoginRequestDto(email = email, password = password))
                }
            tokenStore.saveTokens(response.accessToken, response.refreshToken, response.userId)
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }

    override suspend fun firebaseGoogleAuth(idToken: String): Response<Boolean> =
        try {
            val response: AuthResponseDto =
                httpClient.authorizedRequest(tokenStore) {
                    method = HttpMethod.Post
                    url(ApiConfig.baseUrl + "auth/google")
                    contentType(ContentType.Application.Json)
                    setBody(GoogleAuthRequestDto(idToken = idToken))
                }
            tokenStore.saveTokens(response.accessToken, response.refreshToken, response.userId)
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }

    override suspend fun firebaseSignOut(): Response<Boolean> =
        try {
            val refreshToken = tokenStore.getRefreshToken()
            if (refreshToken != null) {
                try {
                    httpClient.authorizedRequest<Unit>(tokenStore) {
                        method = HttpMethod.Post
                        url(ApiConfig.baseUrl + "auth/logout")
                        contentType(ContentType.Application.Json)
                        setBody(RefreshRequestDto(refreshToken))
                    }
                } catch (e: Exception) {
                    // не страшно, если не удалось отозвать refresh-токен на сервере — главное,
                    // что мы всё равно чистим его локально ниже
                }
            }
            tokenStore.clear()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }

    override suspend fun firebaseForgotPassword(email: String): Response<Boolean> =
        try {
            httpClient.authorizedRequest<Unit>(tokenStore) {
                method = HttpMethod.Post
                url(ApiConfig.baseUrl + "auth/forgot-password")
                contentType(ContentType.Application.Json)
                setBody(ForgotPasswordRequestDto(email = email))
            }
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }

    override fun getGoogleSignInIntent(): Intent = googleSignInClient.signInIntent

    // Блочное тело — внутри нужен bare `return` на случай отсутствия idToken, а это запрещено
    // в функциях с expression body.
    override suspend fun handleGoogleSignInResult(result: ActivityResult): Response<Boolean> {
        return try {
            val account =
                GoogleSignIn
                    .getSignedInAccountFromIntent(result.data)
                    .getResult(GoogleApiException::class.java)

            val idToken =
                account.idToken
                    ?: return Response.Failure(Exception("Google sign-in failed: no ID token"))

            firebaseGoogleAuth(idToken)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }
}
