package com.codekotliners.memify.features.home.presentation.model

sealed interface HomeErrorUiModel {
    data object Network : HomeErrorUiModel

    data object Unknown : HomeErrorUiModel
}
