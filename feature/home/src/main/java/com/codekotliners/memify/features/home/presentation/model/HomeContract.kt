package com.codekotliners.memify.features.home.presentation.model

data class HomePostUiModel(
    val id: String,
    val imageUrl: String,
    val aspectRatio: Float,
    val authorName: String?,
    val authorAvatarUrl: String?,
    val likesCount: Int,
    val isLiked: Boolean,
)

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

enum class HomeErrorUiModel {
    NETWORK,
    UNKNOWN,
}

enum class HomeMessage {
    FEED_REFRESH_FAILED,
    LIKE_UPDATE_FAILED,
}

enum class HomeNavigation {
    AUTH,
}

data class HomeUiState(
    val feed: HomeFeedUiModel = HomeFeedUiModel.Loading,
    val isRefreshing: Boolean = false,
    val pendingLikePostIds: Set<String> = emptySet(),
    val message: HomeMessage? = null,
    val navigation: HomeNavigation? = null,
)

sealed interface HomeAction {
    data object Refresh : HomeAction

    data class LikeClicked(
        val postId: String,
    ) : HomeAction

    data object MessageShown : HomeAction

    data object NavigationHandled : HomeAction
}
