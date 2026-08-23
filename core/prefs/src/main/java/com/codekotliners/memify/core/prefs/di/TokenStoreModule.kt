package com.codekotliners.memify.core.prefs.di

import com.codekotliners.memify.core.prefs.TokenStore
import com.codekotliners.memify.core.prefs.TokenStoreImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TokenStoreModule {
    @Binds
    @Singleton
    abstract fun bindTokenStore(impl: TokenStoreImpl): TokenStore
}
