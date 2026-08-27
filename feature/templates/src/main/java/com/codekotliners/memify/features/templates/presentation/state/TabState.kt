package com.codekotliners.memify.features.templates.presentation.state

import androidx.annotation.StringRes
import com.codekotliners.memify.features.templates.R
import com.codekotliners.memify.core.models.Template

sealed interface TabState {
    object None : TabState

    data object Loading : TabState

    data class Error(
        val type: ErrorType,
    ) : TabState

    data class Content(
        val templates: List<Template>,
        val isLoadingMore: Boolean,
        val reachedEnd: Boolean,
    ) : TabState

    data object Empty : TabState
}

enum class ErrorType(
    @StringRes val titleResId: Int,
    @StringRes val userMessageResId: Int,
) {
    NETWORK(R.string.templates_network_error_title, R.string.templates_network_error_message),
    NEED_LOGIN(R.string.templates_login_required_title, R.string.templates_login_required_message),
    UNKNOWN(R.string.templates_unknown_error_title, R.string.templates_unknown_error_message),
    NEED_LINK_VK(R.string.templates_vk_required_title, R.string.templates_vk_required_message),
    ;
}

enum class Tab(
    @StringRes val nameResId: Int,
) {
    BEST(nameResId = R.string.Best),
    NEW(nameResId = R.string.New),
    FAVOURITE(nameResId = R.string.Favourites),
    VK_IMAGES(nameResId = R.string.templates_tab_vk),
    ;
}
