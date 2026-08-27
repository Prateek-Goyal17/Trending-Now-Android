package com.trending.now.app.feature.me.presentation

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trending.now.app.feature.auth.data.local.AuthSessionStore
import com.trending.now.app.feature.auth.domain.model.AuthState
import com.trending.now.app.feature.auth.domain.repository.BackendAuthRepository
import com.trending.now.app.feature.auth.domain.repository.FirebaseAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MeViewModel @Inject constructor(
    authSessionStore: AuthSessionStore,
    private val backendAuthRepository: BackendAuthRepository,
    private val firebaseAuthRepository: FirebaseAuthRepository,
) : ViewModel() {
    val authState: StateFlow<AuthState> = authSessionStore.authState

    fun logout(context: Context) {
        viewModelScope.launch {
            Log.d(TAG, "Logout requested from Me")
            backendAuthRepository.logout()
            firebaseAuthRepository.signOut(context)
        }
    }

    private companion object {
        const val TAG = "MeViewModel"
    }
}
