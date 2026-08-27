package com.codekotliners.memify.features.home.domain.model

data class HomeAuthor(
    val displayName: String?,
    val avatarUrl: String?,
)

data class HomePost(
    val id: String,
    val imageUrl: String,
    val width: Int,
    val height: Int,
    val likesCount: Int,
    val isLiked: Boolean,
    val author: HomeAuthor,
)

data class HomeLikeUpdate(
    val postId: String,
    val isLiked: Boolean,
    val likesCount: Int,
)

sealed interface HomeFeedResult {
    data class Success(
        val posts: List<HomePost>,
    ) : HomeFeedResult

    data object NetworkFailure : HomeFeedResult

    data object UnknownFailure : HomeFeedResult
}

sealed interface ToggleHomePostLikeResult {
    data object AuthenticationRequired : ToggleHomePostLikeResult

    data class Updated(
        val update: HomeLikeUpdate,
    ) : ToggleHomePostLikeResult
}
