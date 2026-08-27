package com.codekotliners.memify.features.home.presentation.model

data class HomeUiState(
    val feed: HomeFeedUiModel = HomeFeedUiModel.Loading,
    val isRefreshing: Boolean = false,
    val pendingLikePostIds: Set<String> = emptySet(),
    val message: HomeMessage? = null,
    val navigation: HomeNavigation? = null,
)
