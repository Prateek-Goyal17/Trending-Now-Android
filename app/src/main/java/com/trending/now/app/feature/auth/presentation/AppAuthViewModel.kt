package com.trending.now.app.feature.auth.presentation

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trending.now.app.feature.auth.domain.repository.BackendAuthRepository
import com.trending.now.app.feature.auth.domain.repository.FirebaseAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class AppAuthViewModel @Inject constructor(
    private val backendAuthRepository: BackendAuthRepository,
    private val firebaseAuthRepository: FirebaseAuthRepository,
) : ViewModel() {
    init {
        refreshCurrentUser()
    }

    fun refreshCurrentUser() {
        viewModelScope.launch {
            Log.d(TAG, "Startup auth refresh requested")
            backendAuthRepository.refreshCurrentUser()
        }
    }

    fun logout(context: Context) {
        viewModelScope.launch {
            Log.d(TAG, "Logout requested")
            backendAuthRepository.logout()
            firebaseAuthRepository.signOut(context)
        }
    }

    private companion object {
        const val TAG = "AppAuthViewModel"
    }
}
