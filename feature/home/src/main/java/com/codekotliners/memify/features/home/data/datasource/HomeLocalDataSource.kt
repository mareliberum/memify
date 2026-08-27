package com.codekotliners.memify.features.home.data.datasource

import com.codekotliners.memify.core.prefs.TokenStore
import javax.inject.Inject

internal class HomeLocalDataSource @Inject constructor(
    private val tokenStore: TokenStore,
) {
    fun isLoggedIn(): Boolean = tokenStore.isLoggedIn()
}
