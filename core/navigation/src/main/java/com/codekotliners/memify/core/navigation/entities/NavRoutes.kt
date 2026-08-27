package com.codekotliners.memify.core.navigation.entities

import android.net.Uri
import java.util.Base64

sealed class NavRoutes(
    val route: String,
) {
    data object Home : NavRoutes("Home")

    data object Create : NavRoutes("Create") {
        fun createRoute(imageUrl: String? = null): String {
            if (imageUrl.isNullOrEmpty()) return "Create"
            val b64 =
                Base64
                    .getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(imageUrl.toByteArray(Charsets.UTF_8))
            return "Create?image_url=$b64"
        }
    }

    data object Profile : NavRoutes("Profile")

    data object Auth : NavRoutes("Auth")

    data object Register : NavRoutes("Register")

    data object Login : NavRoutes("Login")

    data object Settings :
        NavRoutes(
            "Settings?" +
                "$SETTINGS_INITIAL_AUTHENTICATED={$SETTINGS_INITIAL_AUTHENTICATED}&" +
                "$SETTINGS_INITIAL_DISPLAY_NAME={$SETTINGS_INITIAL_DISPLAY_NAME}&" +
                "$SETTINGS_INITIAL_AVATAR_URL={$SETTINGS_INITIAL_AVATAR_URL}",
        ) {
        fun createRoute(
            isAuthenticated: Boolean,
            displayName: String,
            avatarUrl: String?,
        ): String =
            "$SETTINGS_ROUTE?" +
                "$SETTINGS_INITIAL_AUTHENTICATED=$isAuthenticated&" +
                "$SETTINGS_INITIAL_DISPLAY_NAME=${Uri.encode(displayName)}&" +
                "$SETTINGS_INITIAL_AVATAR_URL=${Uri.encode(avatarUrl.orEmpty())}"
    }

    data object AboutApp : NavRoutes("AboutApp")

    companion object {
        const val IMAGE_TYPE = "imageType"
        const val IMAGE_ID = "imageId"
        const val IMAGE_URL = "image_url"
        const val SETTINGS_INITIAL_AUTHENTICATED = "settings_initial_authenticated"
        const val SETTINGS_INITIAL_DISPLAY_NAME = "settings_initial_display_name"
        const val SETTINGS_INITIAL_AVATAR_URL = "settings_initial_avatar_url"

        private const val SETTINGS_ROUTE = "Settings"
    }

    data object ImageViewer : NavRoutes("ImageViewer/{$IMAGE_TYPE}/{$IMAGE_ID}") {
        fun createRoute(type: ImageType, id: String): String =
            "ImageViewer/${type.name}/$id"
    }
}
