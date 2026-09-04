package com.trending.now.app.feature.me.data.repository

import com.trending.now.app.core.network.TrendingNowNetworkConfig
import com.trending.now.app.feature.me.data.remote.MeApiService
import com.trending.now.app.feature.me.data.remote.ReportResponse
import com.trending.now.app.feature.me.domain.repository.ReportRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReportRepositoryImpl @Inject constructor(
    private val meApiService: MeApiService
) : ReportRepository {
    override suspend fun reportProblem(
        message: String,
        imageFile: File?
    ): Result<ReportResponse> {
        return runCatching {
            val feedbackPart = message.toRequestBody("text/plain".toMediaTypeOrNull())
            val versionPart = TrendingNowNetworkConfig.API_VERSION.toRequestBody("text/plain".toMediaTypeOrNull())
            val platformPart = "android".toRequestBody("text/plain".toMediaTypeOrNull())
            
            val imagePart = imageFile?.let {
                val requestFile = it.asRequestBody("image/*".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("feedbackImage", it.name, requestFile)
            }

            val response = meApiService.reportProblem(
                feedbackMessage = feedbackPart,
                version = versionPart,
                platform = platformPart,
                feedbackImage = imagePart
            )

            if (!response.isSuccessful) {
                error("Submission failed: ${response.code()}")
            }

            response.body() ?: error("Empty response")
        }
    }
}
