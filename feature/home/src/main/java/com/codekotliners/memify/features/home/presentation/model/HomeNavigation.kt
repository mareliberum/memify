package com.codekotliners.memify.features.home.presentation.model

sealed interface HomeNavigation {
    data object Auth : HomeNavigation
}
