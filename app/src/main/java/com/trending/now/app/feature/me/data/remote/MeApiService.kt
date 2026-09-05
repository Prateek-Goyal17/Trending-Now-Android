package com.trending.now.app.feature.me.data.remote

import com.trending.now.app.core.constants.TrendingNowApiPaths
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MeApiService {
    @Multipart
    @POST(TrendingNowApiPaths.REPORT)
    suspend fun reportProblem(
        @Part("feedbackMessage") feedbackMessage: RequestBody,
        @Part("appMeta[version]") version: RequestBody,
        @Part("appMeta[platform]") platform: RequestBody,
        @Part feedbackImage: MultipartBody.Part?
    ): Response<ReportResponse>
}
