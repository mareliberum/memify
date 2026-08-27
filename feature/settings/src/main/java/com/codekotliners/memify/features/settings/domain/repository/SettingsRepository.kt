package com.codekotliners.memify.features.settings.domain.repository

import com.codekotliners.memify.features.settings.domain.model.SettingsAccount
import com.codekotliners.memify.features.settings.domain.model.SettingsTheme
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun observeTheme(): Flow<SettingsTheme>

    fun isAuthenticated(): Boolean

    fun setTheme(theme: SettingsTheme)

    suspend fun getAccount(): SettingsAccount

    suspend fun updateDisplayName(displayName: String)

    suspend fun signOut()
}
