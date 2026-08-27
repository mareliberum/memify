package com.codekotliners.memify.features.settings.data.di

import com.codekotliners.memify.features.settings.data.repository.DefaultAboutAppRepository
import com.codekotliners.memify.features.settings.data.repository.DefaultSettingsRepository
import com.codekotliners.memify.features.settings.domain.repository.AboutAppRepository
import com.codekotliners.memify.features.settings.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class SettingsDataModule {
    @Binds
    @Singleton
    abstract fun bindAboutAppRepository(repository: DefaultAboutAppRepository): AboutAppRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(repository: DefaultSettingsRepository): SettingsRepository
}
