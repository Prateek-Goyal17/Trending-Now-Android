package com.trending.now.app.feature.auth.presentation

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.NoCredentialException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthException
import com.trending.now.app.feature.auth.domain.model.AuthUser
import com.trending.now.app.feature.auth.domain.model.NoGoogleCredentialException
import com.trending.now.app.feature.auth.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val isGoogleLoading: Boolean = false,
    val user: AuthUser? = null,
)

sealed interface LoginUiEvent {
    data class ShowSnackbar(val message: String) : LoginUiEvent
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _events = Channel<LoginUiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun signInWithGoogle(
        context: Context,
        serverClientId: String,
    ) {
        if (_uiState.value.isGoogleLoading) return

        _uiState.update {
            it.copy(
                isGoogleLoading = true,
            )
        }

        viewModelScope.launch {
            Log.d(TAG, "Google sign-in requested")
            authRepository.signInWithGoogle(
                context = context,
                serverClientId = serverClientId,
            ).onSuccess { user ->
                Log.d(TAG, "Google sign-in completed")
                _uiState.update {
                    it.copy(
                        isGoogleLoading = false,
                        user = user,
                    )
                }
            }.onFailure { error ->
                Log.d(TAG, "Google sign-in failed: ${error::class.java.simpleName}")
                _uiState.update {
                    it.copy(
                        isGoogleLoading = false,
                    )
                }
                _events.send(LoginUiEvent.ShowSnackbar(error.toLoginMessage()))
            }
        }
    }

    fun consumeSignedInUser() {
        _uiState.update { it.copy(user = null) }
    }

    private fun Throwable.toLoginMessage(): String {
        return when (this) {
            is GetCredentialCancellationException -> "Google sign-in was cancelled."
            is NoGoogleCredentialException -> "No Google account was selected."
            is NoCredentialException -> "No Google account was found on this device."
            is FirebaseNetworkException -> "Please check your internet connection and try again."
            is FirebaseAuthException -> "Google sign-in failed. Please try again."
            else -> "Unable to sign in with Google. Please try again."
        }
    }

    private companion object {
        const val TAG = "LoginViewModel"
    }
}
