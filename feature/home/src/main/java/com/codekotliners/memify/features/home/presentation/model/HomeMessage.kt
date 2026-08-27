package com.codekotliners.memify.features.home.presentation.model

sealed interface HomeMessage {
    data object FeedRefreshFailed : HomeMessage

    data object LikeUpdateFailed : HomeMessage
}
