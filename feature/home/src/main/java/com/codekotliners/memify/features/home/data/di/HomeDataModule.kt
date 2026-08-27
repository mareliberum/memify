package com.codekotliners.memify.features.home.data.di

import com.codekotliners.memify.features.home.data.repository.DefaultHomeRepository
import com.codekotliners.memify.features.home.domain.repository.HomeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class HomeDataModule {
    @Binds
    @ViewModelScoped
    abstract fun bindHomeRepository(repository: DefaultHomeRepository): HomeRepository
}
