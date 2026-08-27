package com.codekotliners.memify.features.settings.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.codekotliners.memify.core.navigation.entities.AppRoute
import com.codekotliners.memify.features.settings.domain.model.SettingsAccount
import com.codekotliners.memify.features.settings.domain.repository.SettingsRepository
import com.codekotliners.memify.features.settings.presentation.model.SettingsAccountUiModel
import com.codekotliners.memify.features.settings.presentation.model.SettingsAction
import com.codekotliners.memify.features.settings.presentation.model.SettingsMessage
import com.codekotliners.memify.features.settings.presentation.model.SettingsNameError
import com.codekotliners.memify.features.settings.presentation.model.SettingsNavigation
import com.codekotliners.memify.features.settings.presentation.model.SettingsThemeUiModel
import com.codekotliners.memify.features.settings.presentation.model.SettingsUiState
import com.codekotliners.memify.features.settings.presentation.model.toDomain
import com.codekotliners.memify.features.settings.presentation.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: SettingsRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _uiState = MutableStateFlow(savedStateHandle.initialSettingsState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private var accountJob: Job? = null

    init {
        observeTheme()
        refreshAccount()
    }

    fun onAction(action: SettingsAction) {
        when (action) {
            SettingsAction.Refresh -> refreshAccount()
            SettingsAction.AuthChanged -> refreshAccount(notifyProfile = true)
            is SettingsAction.ThemeSelected -> setTheme(action.theme)
            is SettingsAction.NameChanged -> changeName(action.name)
            SettingsAction.SaveNameClicked -> saveName(_uiState.value.nameInput)
            is SettingsAction.VkNameSelected -> useVkName(action.name)
            SettingsAction.LoginClicked -> navigate(SettingsNavigation.OpenLogin)
            SettingsAction.SignOutRequested -> setSignOutConfirmation(visible = true)
            SettingsAction.SignOutDismissed -> setSignOutConfirmation(visible = false)
            SettingsAction.SignOutConfirmed -> signOut()
            SettingsAction.MessageShown -> _uiState.update { state -> state.copy(message = null) }
            SettingsAction.NavigationHandled -> _uiState.update { state -> state.copy(navigation = null) }
        }
    }

    private fun observeTheme() {
        repository
            .observeTheme()
            .onEach { theme ->
                _uiState.update { state -> state.copy(selectedTheme = theme.toUiModel()) }
            }.launchIn(viewModelScope)
    }

    private fun refreshAccount(notifyProfile: Boolean = false) {
        accountJob?.cancel()
        accountJob =
            viewModelScope.launch {
                _uiState.update { state -> state.copy(isLoading = true) }

                try {
                    val account = repository.getAccount()
                    val displayName = (account as? SettingsAccount.Authenticated)?.displayName.orEmpty()
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            account = account.toUiModel(),
                            nameInput = displayName,
                            nameError = null,
                            navigation =
                                if (notifyProfile) {
                                    SettingsNavigation.AccountChanged
                                } else {
                                    state.navigation
                                },
                        )
                    }
                } catch (exception: CancellationException) {
                    throw exception
                } catch (_: Exception) {
                    val fallbackAccount =
                        if (repository.isAuthenticated()) {
                            SettingsAccountUiModel.Authenticated(
                                displayName = _uiState.value.displayName,
                                avatarUrl = _uiState.value.avatarUrl,
                            )
                        } else {
                            SettingsAccountUiModel.Guest
                        }
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            account = fallbackAccount,
                            message = SettingsMessage.AccountLoadFailed,
                            navigation =
                                if (notifyProfile) {
                                    SettingsNavigation.AccountChanged
                                } else {
                                    state.navigation
                                },
                        )
                    }
                }
            }
    }

    private fun setTheme(theme: SettingsThemeUiModel) {
        repository.setTheme(theme.toDomain())
        _uiState.update { state -> state.copy(selectedTheme = theme) }
    }

    private fun changeName(name: String) {
        _uiState.update { state -> state.copy(nameInput = name, nameError = null) }
    }

    private fun useVkName(name: String) {
        if (name.isBlank()) {
            _uiState.update { state -> state.copy(message = SettingsMessage.VkNameUnavailable) }
            return
        }

        _uiState.update { state -> state.copy(nameInput = name, nameError = null) }
        saveName(name)
    }

    private fun saveName(rawName: String) {
        if (_uiState.value.isNameSaving) return

        val displayName = rawName.trim()
        if (displayName.isEmpty()) {
            _uiState.update { state -> state.copy(nameError = SettingsNameError.Empty) }
            return
        }
        if (displayName == _uiState.value.displayName) return

        viewModelScope.launch {
            _uiState.update { state -> state.copy(isNameSaving = true, nameError = null) }
            try {
                repository.updateDisplayName(displayName)
                _uiState.update { state ->
                    state.copy(
                        account =
                            SettingsAccountUiModel.Authenticated(
                                displayName = displayName,
                                avatarUrl = state.avatarUrl,
                            ),
                        nameInput = displayName,
                        isNameSaving = false,
                        message = SettingsMessage.NameUpdated,
                        navigation = SettingsNavigation.AccountChanged,
                    )
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                _uiState.update { state ->
                    state.copy(
                        isNameSaving = false,
                        message = SettingsMessage.NameUpdateFailed,
                    )
                }
            }
        }
    }

    private fun setSignOutConfirmation(visible: Boolean) {
        _uiState.update { state -> state.copy(isSignOutConfirmationVisible = visible) }
    }

    private fun signOut() {
        if (_uiState.value.isSigningOut) return

        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    isSigningOut = true,
                    isSignOutConfirmationVisible = false,
                )
            }
            try {
                repository.signOut()
                _uiState.update { state ->
                    state.copy(
                        account = SettingsAccountUiModel.Guest,
                        nameInput = "",
                        isSigningOut = false,
                        message = SettingsMessage.SignedOut,
                        navigation = SettingsNavigation.AccountChanged,
                    )
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                _uiState.update { state ->
                    state.copy(
                        isSigningOut = false,
                        message = SettingsMessage.SignOutFailed,
                    )
                }
            }
        }
    }

    private fun navigate(navigation: SettingsNavigation) {
        _uiState.update { state -> state.copy(navigation = navigation) }
    }
}

private fun SavedStateHandle.initialSettingsState(): SettingsUiState {
    val route = toRoute<AppRoute.Settings>()
    val account =
        if (route.isAuthenticated) {
            SettingsAccountUiModel.Authenticated(
                displayName = route.displayName,
                avatarUrl = route.avatarUrl?.takeIf(String::isNotBlank),
            )
        } else {
            SettingsAccountUiModel.Guest
        }

    return SettingsUiState(
        account = account,
        nameInput = route.displayName.takeIf { route.isAuthenticated }.orEmpty(),
    )
}
