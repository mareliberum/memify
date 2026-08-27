package com.codekotliners.memify.features.profile.data.di

import com.codekotliners.memify.features.profile.data.repository.DefaultProfileRepository
import com.codekotliners.memify.features.profile.domain.repository.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class ProfileDataModule {
    @Binds
    @Singleton
    abstract fun bindProfileRepository(repository: DefaultProfileRepository): ProfileRepository
}
