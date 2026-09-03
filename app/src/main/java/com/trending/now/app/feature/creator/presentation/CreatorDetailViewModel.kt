package com.trending.now.app.feature.creator.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trending.now.app.feature.creator.data.remote.CreatorDetailResponse
import com.trending.now.app.feature.creator.domain.repository.CreatorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class CreatorDetailTab {
    AllFeed,
    News,
    Lifestyle,
}

data class CreatorDetailUiState(
    val isLoading: Boolean = false,
    val creatorDetail: CreatorDetailResponse? = null,
    val selectedTab: CreatorDetailTab = CreatorDetailTab.AllFeed,
    val errorMessage: String? = null,
)

@HiltViewModel
class CreatorDetailViewModel @Inject constructor(
    private val creatorRepository: CreatorRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreatorDetailUiState())
    val uiState: StateFlow<CreatorDetailUiState> = _uiState.asStateFlow()

    private var loadedCreatorSlug: String? = null

    fun loadCreatorDetail(creatorSlug: String) {
        if (creatorSlug.isBlank()) return
        if (_uiState.value.isLoading || loadedCreatorSlug == creatorSlug) return

        loadedCreatorSlug = creatorSlug
        _uiState.update {
            it.copy(
                isLoading = true,
                errorMessage = null,
            )
        }

        viewModelScope.launch {
            creatorRepository.getCreatorDetail(creatorSlug)
                .onSuccess { response ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            creatorDetail = response,
                            errorMessage = null,
                        )
                    }
                }
                .onFailure {
                    loadedCreatorSlug = null
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Unable to load creator.",
                        )
                    }
                }
        }
    }

    fun selectTab(tab: CreatorDetailTab) {
        _uiState.update {
            it.copy(selectedTab = tab)
        }
    }
}
