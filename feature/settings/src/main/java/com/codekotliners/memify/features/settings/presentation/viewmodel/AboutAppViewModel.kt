package com.codekotliners.memify.features.settings.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.codekotliners.memify.features.settings.domain.repository.AboutAppRepository
import com.codekotliners.memify.features.settings.presentation.model.AboutAppUiState
import com.codekotliners.memify.features.settings.presentation.model.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AboutAppViewModel @Inject constructor(
    repository: AboutAppRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(repository.getInfo().toUiState())
    val uiState: StateFlow<AboutAppUiState> = _uiState.asStateFlow()
}
