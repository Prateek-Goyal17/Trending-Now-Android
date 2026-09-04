package com.trending.now.app.feature.me.domain.repository

import com.trending.now.app.feature.me.data.remote.ReportResponse
import java.io.File

interface ReportRepository {
    suspend fun reportProblem(
        message: String,
        imageFile: File?
    ): Result<ReportResponse>
}
