package com.codekotliners.memify.core.network.models

import kotlinx.serialization.Serializable

// DTO для /auth/* — соответствуют RegisterRequest/LoginRequest/... в backend/src/main/kotlin/routes/AuthRoutes.kt

@Serializable
data class RegisterRequestDto(val email: String, val password: String, val username: String)

@Serializable
data class LoginRequestDto(val email: String, val password: String)

@Serializable
data class GoogleAuthRequestDto(val idToken: String)

@Serializable
data class RefreshRequestDto(val refreshToken: String)

@Serializable
data class RefreshResponseDto(val accessToken: String)

@Serializable
data class ForgotPasswordRequestDto(val email: String)

@Serializable
data class ResetPasswordRequestDto(val token: String, val newPassword: String)

@Serializable
data class AuthResponseDto(
    val accessToken: String,
    val refreshToken: String,
    val userId: String,
    val username: String,
    val email: String,
    val photoUrl: String?,
)

@Serializable
data class ErrorResponseDto(val error: String)
