package com.trending.now.app.feature.auth.data.remote

import com.trending.now.app.core.constants.TrendingNowApiPaths
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthApiService {
    @POST(TrendingNowApiPaths.USER)
    suspend fun registerOrLogin(
        @Body body: RegisterOrLoginRequest,
    ): Response<ResponseBody>

    @GET(TrendingNowApiPaths.USER)
    suspend fun getCurrentUser(): Response<ResponseBody>

    @PATCH(TrendingNowApiPaths.USER_BY_UID)
    suspend fun updateUser(
        @Path("uid") uid: String,
        @Body body: UpdateUserRequest,
    ): Response<ResponseBody>
}
