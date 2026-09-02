package com.trending.now.app.feature.creator.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trending.now.app.feature.auth.data.local.AuthSessionStore
import com.trending.now.app.feature.auth.domain.model.AuthState
import com.trending.now.app.feature.creator.data.remote.CreatorScreenResponse
import com.trending.now.app.feature.creator.domain.repository.CreatorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CreatorUiState(
    val authState: AuthState = AuthState.Guest,
    val isLoading: Boolean = false,
    val creatorScreenFeed: CreatorScreenResponse? = null,
)

@HiltViewModel
class CreatorViewModel @Inject constructor(
    private val creatorRepository: CreatorRepository,
    private val authSessionStore: AuthSessionStore,
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        CreatorUiState(
            authState = authSessionStore.authState.value,
        ),
    )
    val uiState: StateFlow<CreatorUiState> = _uiState.asStateFlow()

    init {
        observeAuthentication()
    }

    private fun observeAuthentication() {
        viewModelScope.launch {
            authSessionStore.authState.collectLatest { authState ->
                _uiState.update {
                    it.copy(authState = authState)
                }
                fetchCreatorScreenFeed()
            }
        }
    }

    private suspend fun fetchCreatorScreenFeed() {
        _uiState.update {
            it.copy(isLoading = true)
        }

        creatorRepository.getCreatorScreenFeed()
            .onSuccess { response ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        creatorScreenFeed = response,
                    )
                }
            }
            .onFailure {
                _uiState.update {
                    it.copy(isLoading = false)
                }
            }
    }
}
