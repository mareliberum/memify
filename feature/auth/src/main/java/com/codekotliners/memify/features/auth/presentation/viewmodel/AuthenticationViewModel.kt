package com.codekotliners.memify.features.auth.presentation.viewmodel

import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codekotliners.memify.core.common.Response
import com.codekotliners.memify.features.auth.di.GoogleWebClientId
import com.codekotliners.memify.features.auth.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthState {
    data object Loading : AuthState()

    data object Authenticated : AuthState()

    data object Unauthenticated : AuthState()

    class Error(
        val exception: Exception,
    ) : AuthState()
}

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val repository: AuthRepository,
    @GoogleWebClientId val webClientId: String,
) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState

    init {
        checkCurrentUser()
    }

    fun checkCurrentUser() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val isAuthenticated = repository.getCurrentUser() != null
                _authState.value =
                    if (isAuthenticated) {
                        AuthState.Authenticated
                    } else {
                        AuthState.Unauthenticated
                    }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e)
            }
        }
    }

    fun getGoogleSignInIntent(): Intent = repository.getGoogleSignInIntent()

    fun onLogInWithGoogle(idToken: String) =
        handleAuthRequest {
            repository.firebaseGoogleAuth(idToken)
        }

    fun handleGoogleSignInResult(result: ActivityResult) =
        handleAuthRequest {
            val res: Response<Boolean> = repository.handleGoogleSignInResult(result)
            res
        }

    fun onLogInWithMail(email: String, password: String) =
        handleAuthRequest {
            repository.firebaseSignIn(email, password)
        }

    private fun handleAuthRequest(block: suspend () -> Response<Boolean>) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            val result =
                try {
                    block()
                } catch (e: Exception) {
                    Response.Failure(e)
                }

            // Раньше результат block() (в т.ч. Response.Failure от неудачного запроса —
            // например, бэк отклонил Google ID-токен или сеть недоступна) здесь просто
            // отбрасывался, и ниже состояние безусловно выставлялось в Unauthenticated —
            // то есть при ошибке экран визуально не менялся вообще, будто ничего не произошло.
            if (result is Response.Failure) {
                _authState.value = AuthState.Error(result.error)
                return@launch
            }

            _authState.value =
                if (repository.getCurrentUser() != null) {
                    AuthState.Authenticated
                } else {
                    AuthState.Unauthenticated
                }
        }
    }

    fun resetSignInState() {
        _authState.value = AuthState.Unauthenticated
    }
}
