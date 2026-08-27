package com.codekotliners.memify.core.navigation.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface AppRoute {
    @Serializable
    @SerialName("home")
    data object Home : AppRoute

    @Serializable
    @SerialName("create")
    data class Create(
        val imageUrl: String? = null,
    ) : AppRoute

    @Serializable
    @SerialName("profile")
    data object Profile : AppRoute

    @Serializable
    @SerialName("auth")
    data object Auth : AppRoute

    @Serializable
    @SerialName("login")
    data object Login : AppRoute

    @Serializable
    @SerialName("register")
    data object Register : AppRoute

    @Serializable
    @SerialName("settings")
    data class Settings(
        val isAuthenticated: Boolean = false,
        val displayName: String = "",
        val avatarUrl: String? = null,
    ) : AppRoute

    @Serializable
    @SerialName("about_app")
    data object AboutApp : AppRoute

    @Serializable
    @SerialName("image_viewer")
    data class ImageViewer(
        val imageId: String,
    ) : AppRoute
}
