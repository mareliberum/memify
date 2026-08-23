package com.codekotliners.memify.core.network.di

import android.content.Context
import com.codekotliners.memify.core.network.utils.InternetChecker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideInternetChecker(
        @ApplicationContext context: Context,
    ): InternetChecker = InternetChecker(context)

    // Единый HTTP-клиент вместо Firebase SDK — все REST-датасорсы (posts/users/auth/templates)
    // ходят через него, см. core/network/api/RequestUtils.kt.
    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient =
        HttpClient(OkHttp) {
            expectSuccess = false
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        encodeDefaults = true
                        isLenient = true
                    },
                )
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 15_000
                connectTimeoutMillis = 10_000
            }
        }
}
