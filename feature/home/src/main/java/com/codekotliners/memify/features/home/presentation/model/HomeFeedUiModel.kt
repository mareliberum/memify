package com.codekotliners.memify.features.home.presentation.model

sealed interface HomeFeedUiModel {
    data object Loading : HomeFeedUiModel

    data object Empty : HomeFeedUiModel

    data class Content(
        val posts: List<HomePostUiModel>,
    ) : HomeFeedUiModel

    data class Error(
        val type: HomeErrorUiModel,
    ) : HomeFeedUiModel
}
