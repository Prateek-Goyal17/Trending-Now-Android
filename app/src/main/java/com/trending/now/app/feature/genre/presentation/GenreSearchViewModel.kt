package com.trending.now.app.feature.genre.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trending.now.app.feature.genre.domain.model.Genre
import com.trending.now.app.feature.genre.domain.model.GenreCreator
import com.trending.now.app.feature.genre.domain.repository.GenreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GenreSearchUiState(
    val isLoading: Boolean = false,
    val genres: List<Genre> = emptyList(),
    val query: String = "",
    val errorMessage: String? = null,
) {
    val searchResults: List<GenreCreator>
        get() {
            val normalizedQuery = query.trim().lowercase()
            if (normalizedQuery.isEmpty()) return emptyList()

            return genres
                .flatMap { genre ->
                    genre.creators.map { creator -> genre to creator }
                }
                .filter { (genre, creator) ->
                    creator.name.replace('_', ' ').contains(normalizedQuery, ignoreCase = true) ||
                        creator.role.contains(normalizedQuery, ignoreCase = true) ||
                        genre.name.contains(normalizedQuery, ignoreCase = true)
                }
                .map { (_, creator) -> creator }
                .distinctBy(GenreCreator::id)
        }
}

@HiltViewModel
class GenreSearchViewModel @Inject constructor(
    private val genreRepository: GenreRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(GenreSearchUiState())
    val uiState: StateFlow<GenreSearchUiState> = _uiState.asStateFlow()

    init {
        loadGenres()
    }

    fun loadGenres() {
        if (_uiState.value.isLoading) return

        _uiState.update {
            it.copy(
                isLoading = true,
                errorMessage = null,
            )
        }

        viewModelScope.launch {
            genreRepository.getGenres()
                .onSuccess { genres ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            genres = genres,
                            errorMessage = null,
                        )
                    }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Unable to load genres. Please try again.",
                        )
                    }
                }
        }
    }

    fun updateQuery(query: String) {
        _uiState.update { it.copy(query = query) }
    }
}
