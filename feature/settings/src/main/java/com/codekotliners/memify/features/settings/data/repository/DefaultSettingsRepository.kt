package com.codekotliners.memify.features.settings.data.repository

import com.codekotliners.memify.features.settings.data.datasource.SettingsLocalDataSource
import com.codekotliners.memify.features.settings.data.datasource.SettingsRemoteDataSource
import com.codekotliners.memify.features.settings.data.mapper.toData
import com.codekotliners.memify.features.settings.data.mapper.toDomain
import com.codekotliners.memify.features.settings.domain.model.SettingsAccount
import com.codekotliners.memify.features.settings.domain.model.SettingsTheme
import com.codekotliners.memify.features.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DefaultSettingsRepository @Inject constructor(
    private val localDataSource: SettingsLocalDataSource,
    private val remoteDataSource: SettingsRemoteDataSource,
) : SettingsRepository {
    override fun observeTheme(): Flow<SettingsTheme> =
        localDataSource.observeTheme().map { themeMode -> themeMode.toDomain() }

    override fun isAuthenticated(): Boolean = localDataSource.isAuthenticated()

    override fun setTheme(theme: SettingsTheme) {
        localDataSource.setTheme(theme.toData())
    }

    override suspend fun getAccount(): SettingsAccount =
        if (localDataSource.isAuthenticated()) {
            remoteDataSource.getAccount().toDomain()
        } else {
            SettingsAccount.Guest
        }

    override suspend fun updateDisplayName(displayName: String) {
        remoteDataSource.updateDisplayName(displayName)
    }

    override suspend fun signOut() {
        try {
            remoteDataSource.revokeSession()
        } catch (exception: CancellationException) {
            throw exception
        } catch (_: Exception) {
            // Локальная сессия должна завершиться даже при недоступном сервере.
        } finally {
            localDataSource.clearSession()
        }
    }
}
