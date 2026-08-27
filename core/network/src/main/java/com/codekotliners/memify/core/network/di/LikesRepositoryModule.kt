package com.codekotliners.memify.core.network.di

import com.codekotliners.memify.core.repositories.likes.LikesRepositoryImpl
import com.codekotliners.memify.core.repositories.likes.LikesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LikesRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindLikesRepository(impl: LikesRepositoryImpl): LikesRepository
}
