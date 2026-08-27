package com.codekotliners.memify.features.home.presentation.state

import androidx.annotation.StringRes
import com.codekotliners.memify.core.models.Post
import com.codekotliners.memify.features.home.R

enum class MainFeedTab(
    @StringRes val nameResId: Int,
) {
    POPULAR(R.string.popular_tab_name),
    NEW(R.string.new_tab_name),
    ;
}

sealed interface PostsFeedTabState {
    object None : PostsFeedTabState

    object Empty : PostsFeedTabState

    data object Loading : PostsFeedTabState

    data class Error(
        val type: ErrorType,
    ) : PostsFeedTabState

    data class Content(
        val posts: List<Post>,
    ) : PostsFeedTabState
}

enum class ErrorType(
    @StringRes val titleResId: Int,
    @StringRes val userMessageResId: Int,
) {
    NETWORK(
        titleResId = R.string.network_error_title,
        userMessageResId = R.string.network_errormessage,
    ),
    UNKNOWN(
        titleResId = R.string.unknown_error_title,
        userMessageResId = R.string.unknown_error_message,
    ),
    ;
}
