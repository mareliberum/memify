package com.codekotliners.memify.features.auth.domain.repository

import android.content.Intent
import androidx.activity.result.ActivityResult
import com.codekotliners.memify.core.common.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    fun getAuthState(viewModelScope: CoroutineScope): StateFlow<Boolean>

    // Раньше возвращал FirebaseUser? — теперь возвращает id пользователя из TokenStore (или
    // null, если не залогинен). Вызывающий код (`repository.getCurrentUser() != null`) не поменялся.
    suspend fun getCurrentUser(): String?

    suspend fun firebaseCreateAccount(name: String, email: String, password: String): Response<Boolean>

    suspend fun firebaseSignIn(email: String, password: String): Response<Boolean>

    suspend fun firebaseGoogleAuth(idToken: String): Response<Boolean>

    suspend fun firebaseSignOut(): Response<Boolean>

    suspend fun firebaseForgotPassword(email: String): Response<Boolean>

    fun getGoogleSignInIntent(): Intent

    suspend fun handleGoogleSignInResult(result: ActivityResult): Response<Boolean>
}
