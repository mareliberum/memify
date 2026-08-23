package com.codekotliners.memify.features.auth.data.repository

import android.content.Context
import com.codekotliners.memify.core.prefs.TokenStore
import com.codekotliners.memify.features.auth.di.GoogleWebClientId
import com.codekotliners.memify.features.auth.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Provides
    fun provideAuthRepository(
        httpClient: HttpClient,
        tokenStore: TokenStore,
        @ApplicationContext context: Context,
        @GoogleWebClientId webClientId: String,
    ): AuthRepository = AuthRepositoryImpl(httpClient, tokenStore, context, webClientId)
}
