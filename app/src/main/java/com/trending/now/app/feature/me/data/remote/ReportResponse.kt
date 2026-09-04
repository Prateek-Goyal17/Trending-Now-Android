package com.trending.now.app.feature.me.data.remote

import com.google.gson.annotations.SerializedName

data class ReportResponse(
    val success: Boolean,
    val data: ReportData?
)

data class ReportData(
    @SerializedName("_id")
    val id: String?,
    val feedbackMessage: String?,
    val feedbackImage: String?,
    val createdAt: String?,
    val updatedAt: String?
)
