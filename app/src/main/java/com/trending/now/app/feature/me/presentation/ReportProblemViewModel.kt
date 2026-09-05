package com.trending.now.app.feature.me.presentation

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trending.now.app.feature.me.domain.repository.ReportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

data class ReportProblemUiState(
    val description: String = "",
    val imageUri: Uri? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

@HiltViewModel
class ReportProblemViewModel @Inject constructor(
    private val reportRepository: ReportRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReportProblemUiState())
    val uiState: StateFlow<ReportProblemUiState> = _uiState.asStateFlow()

    fun onDescriptionChange(value: String) {
        if (value.length <= 150) {
            _uiState.update { it.copy(description = value) }
        }
    }

    fun onImageSelected(uri: Uri?) {
        _uiState.update { it.copy(imageUri = uri) }
    }

    fun submitReport(context: Context) {
        val currentState = _uiState.value
        if (currentState.description.isBlank()) {
            _uiState.update { it.copy(error = "Description cannot be empty.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            val imageFile = currentState.imageUri?.let { uriToTempFile(context, it) }
            
            val result = reportRepository.reportProblem(
                message = currentState.description,
                imageFile = imageFile
            )

            result.onSuccess {
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            }.onFailure { error ->
                _uiState.update { it.copy(isLoading = false, error = error.message) }
            }
            
            // Cleanup temp file
            imageFile?.delete()
        }
    }

    fun clearSuccess() {
        _uiState.update { it.copy(isSuccess = false, description = "", imageUri = null) }
    }

    private fun uriToTempFile(context: Context, uri: Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val file = File(context.cacheDir, "temp_report_image_${System.currentTimeMillis()}.jpg")
            inputStream?.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            file
        } catch (_: Exception) {
            null
        }
    }
}
