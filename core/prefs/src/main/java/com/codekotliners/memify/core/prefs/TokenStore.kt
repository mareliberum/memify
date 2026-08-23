package com.codekotliners.memify.core.prefs

/**
 * Хранилище своих JWT-токенов (access + refresh) вместо Firebase Auth, который сам
 * управлял сессией. Реализация — EncryptedSharedPreferences, см. TokenStoreImpl.
 */
interface TokenStore {
    fun getAccessToken(): String?

    fun getRefreshToken(): String?

    fun getUserId(): String?

    fun saveTokens(accessToken: String, refreshToken: String, userId: String)

    fun saveAccessToken(accessToken: String)

    fun clear()

    fun isLoggedIn(): Boolean
}
