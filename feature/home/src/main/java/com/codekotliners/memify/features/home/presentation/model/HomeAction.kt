package com.codekotliners.memify.features.home.presentation.model

sealed interface HomeAction {
    data object Refresh : HomeAction

    data class LikeClicked(
        val postId: String,
    ) : HomeAction

    data object MessageShown : HomeAction

    data object NavigationHandled : HomeAction
}
