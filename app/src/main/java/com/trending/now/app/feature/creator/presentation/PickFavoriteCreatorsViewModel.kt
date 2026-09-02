package com.trending.now.app.feature.creator.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trending.now.app.feature.auth.data.local.AuthSessionStore
import com.trending.now.app.feature.auth.domain.model.AuthState
import com.trending.now.app.feature.creator.domain.repository.CreatorRepository
import com.trending.now.app.feature.genre.domain.model.Genre
import com.trending.now.app.feature.genre.domain.model.GenreCreator
import com.trending.now.app.feature.genre.domain.repository.GenreRepository
import com.trending.now.app.feature.user.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class FavoriteCreatorPickerAccess {
    GuestLocked,
    NewUser,
    ExistingUser,
}

data class PickFavoriteCreatorsUiState(
    val access: FavoriteCreatorPickerAccess = FavoriteCreatorPickerAccess.GuestLocked,
    val isLoading: Boolean = false,
    val genres: List<Genre> = emptyList(),
    val selectedGenreId: String? = null,
    val selectedCreatorIds: Set<String> = emptySet(),
    val isSubmitting: Boolean = false,
    val submissionStarted: Boolean = false,
    val errorMessage: String? = null,
) {
    val visibleCreators: List<GenreCreator>
        get() = genres.firstOrNull { it.id == selectedGenreId }?.creators.orEmpty()

    val canChangeSelection: Boolean
        get() = access == FavoriteCreatorPickerAccess.NewUser &&
            !isSubmitting &&
            !submissionStarted

    val canContinue: Boolean
        get() = access == FavoriteCreatorPickerAccess.NewUser &&
            selectedCreatorIds.isNotEmpty() &&
            !isSubmitting
}

sealed interface PickFavoriteCreatorsEvent {
    data object Completed : PickFavoriteCreatorsEvent
}

@HiltViewModel
class PickFavoriteCreatorsViewModel @Inject constructor(
    private val genreRepository: GenreRepository,
    private val creatorRepository: CreatorRepository,
    private val userRepository: UserRepository,
    private val authSessionStore: AuthSessionStore,
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        PickFavoriteCreatorsUiState(
            access = authSessionStore.authState.value.toPickerAccess(),
        ),
    )
    val uiState: StateFlow<PickFavoriteCreatorsUiState> = _uiState.asStateFlow()

    private val _events = Channel<PickFavoriteCreatorsEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var pendingSubmissionIds: Set<String> = emptySet()
    private var completionSent = false

    init {
        observeAuthentication()
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
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            genres = genres,
                            selectedGenreId = state.selectedGenreId
                                ?.takeIf { selectedId -> genres.any { it.id == selectedId } }
                                ?: genres.firstOrNull()?.id,
                            errorMessage = null,
                        )
                    }
                }
                .onFailure {
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            errorMessage = "Unable to load creators. Please try again.",
                        )
                    }
                }
        }
    }

    fun selectGenre(genreId: String) {
        if (_uiState.value.genres.none { it.id == genreId }) return

        _uiState.update {
            it.copy(selectedGenreId = genreId)
        }
    }

    fun toggleCreator(creatorId: String) {
        if (!_uiState.value.canChangeSelection) return

        _uiState.update { state ->
            val updatedSelection = state.selectedCreatorIds.toMutableSet().apply {
                if (!add(creatorId)) remove(creatorId)
            }
            state.copy(selectedCreatorIds = updatedSelection)
        }
    }

    fun continueWithSelection() {
        val state = _uiState.value
        if (!state.canContinue) return

        val idsToSubmit = pendingSubmissionIds.ifEmpty { state.selectedCreatorIds }
        if (idsToSubmit.isEmpty()) return

        _uiState.update {
            it.copy(
                isSubmitting = true,
                submissionStarted = true,
                errorMessage = null,
            )
        }

        viewModelScope.launch {
            val failedIds = buildSet {
                idsToSubmit.forEach { creatorId ->
                    if (creatorRepository.addFavoriteCreator(creatorId).isFailure) {
                        add(creatorId)
                    }
                }
            }

            if (failedIds.isNotEmpty()) {
                pendingSubmissionIds = failedIds
                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        errorMessage = "Some creators could not be saved. Please try again.",
                    )
                }
                return@launch
            }

            pendingSubmissionIds = emptySet()
            userRepository.refreshCurrentUser()
                .onFailure {
                    authSessionStore.profile.value?.let { profile ->
                        authSessionStore.saveProfile(
                            profile.copy(
                                favoriteCreatorsCount = maxOf(
                                    profile.favoriteCreatorsCount,
                                    state.selectedCreatorIds.size,
                                ),
                            ),
                        )
                    }
                }

            _uiState.update {
                it.copy(isSubmitting = false)
            }
            completeOnce()
        }
    }

    private fun observeAuthentication() {
        viewModelScope.launch {
            authSessionStore.authState.collect { authState ->
                val access = authState.toPickerAccess()
                _uiState.update {
                    it.copy(access = access)
                }

                if (
                    access == FavoriteCreatorPickerAccess.ExistingUser &&
                    !_uiState.value.isSubmitting
                ) {
                    completeOnce()
                }
            }
        }
    }

    private suspend fun completeOnce() {
        if (completionSent) return

        completionSent = true
        _events.send(PickFavoriteCreatorsEvent.Completed)
    }
}

private fun AuthState.toPickerAccess(): FavoriteCreatorPickerAccess {
    return when (this) {
        AuthState.Guest,
        AuthState.LoggedOut,
        -> FavoriteCreatorPickerAccess.GuestLocked

        is AuthState.NewUser -> FavoriteCreatorPickerAccess.NewUser
        is AuthState.OldUser -> FavoriteCreatorPickerAccess.ExistingUser
    }
}
