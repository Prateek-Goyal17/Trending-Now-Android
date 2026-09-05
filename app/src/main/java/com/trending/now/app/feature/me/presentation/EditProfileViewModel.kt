package com.trending.now.app.feature.me.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trending.now.app.feature.auth.data.local.AuthSessionStore
import com.trending.now.app.feature.auth.domain.repository.BackendAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EditProfileUiState(
    val firstName: String = "",
    val lastName: String = "",
    val username: String = "",
    val email: String = "",
    val initialFirstName: String = "",
    val initialLastName: String = "",
    val initialUsername: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
) {
    val hasChanges: Boolean
        get() = firstName != initialFirstName ||
                lastName != initialLastName ||
                username != initialUsername
}

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val authRepository: BackendAuthRepository,
    private val authSessionStore: AuthSessionStore,
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    init {
        // Load initial profile data
        val profile = authSessionStore.profile.value
        if (profile != null) {
            val firstName = profile.firstName.orEmpty()
            val lastName = profile.lastName.orEmpty()
            val username = profile.username.orEmpty()
            val email = profile.email.orEmpty()
            
            _uiState.update {
                it.copy(
                    firstName = firstName,
                    lastName = lastName,
                    username = username,
                    email = email,
                    initialFirstName = firstName,
                    initialLastName = lastName,
                    initialUsername = username
                )
            }
        }
    }

    fun onFirstNameChange(value: String) {
        _uiState.update { it.copy(firstName = value) }
    }

    fun onLastNameChange(value: String) {
        _uiState.update { it.copy(lastName = value) }
    }

    fun onUsernameChange(value: String) {
        _uiState.update { it.copy(username = value) }
    }

    fun updateProfile() {
        val currentState = _uiState.value

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            try {
                val result = authRepository.updateUser(
                    firstName = currentState.firstName,
                    lastName = currentState.lastName,
                    username = currentState.username
                )

                result.onSuccess {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true,
                            initialFirstName = it.firstName,
                            initialLastName = it.lastName,
                            initialUsername = it.username
                        )
                    }
                }.onFailure { error ->
                    android.util.Log.e("EditProfileVM", "Update failed", error)
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
            } catch (e: Exception) {
                android.util.Log.e("EditProfileVM", "Exception in updateProfile", e)
                _uiState.update { it.copy(isLoading = false, error = "An unexpected error occurred: ${e.message}") }
            }
        }
    }

    fun clearSuccess() {
        _uiState.update { it.copy(isSuccess = false) }
    }
}
