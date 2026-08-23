package com.codekotliners.memify.core.network.models

import kotlinx.serialization.Serializable

// Соответствует UserDto/UpdateProfileRequest в backend/src/main/kotlin/routes/UsersRoutes.kt

@Serializable
data class UserDto(
    val id: String,
    val username: String,
    val email: String,
    val photoUrl: String?,
    val phone: String?,
    val tsi: Int,
)

@Serializable
data class UpdateProfileRequestDto(
    val username: String? = null,
    val photoUrl: String? = null,
    val phone: String? = null,
    val tsi: Int? = null,
)
