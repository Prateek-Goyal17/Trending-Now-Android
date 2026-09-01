package com.trending.now.app.feature.creator.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trending.now.app.feature.creator.data.remote.CreatorScreenResponse
import com.trending.now.app.feature.creator.domain.repository.CreatorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CreatorUiState(
    val isLoading: Boolean = false,
    val creatorScreenFeed: CreatorScreenResponse? = null,
)

@HiltViewModel
class CreatorViewModel @Inject constructor(
    private val creatorRepository: CreatorRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreatorUiState())
    val uiState: StateFlow<CreatorUiState> = _uiState.asStateFlow()

    init {
        loadCreatorScreenFeed()
    }

    fun loadCreatorScreenFeed() {
        if (_uiState.value.isLoading) return

        _uiState.update {
            it.copy(isLoading = true)
        }

        viewModelScope.launch {
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
}
