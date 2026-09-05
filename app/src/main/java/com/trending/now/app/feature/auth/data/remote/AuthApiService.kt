package com.trending.now.app.feature.auth.data.remote

import com.trending.now.app.core.constants.TrendingNowApiPaths
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface AuthApiService {
    @POST(TrendingNowApiPaths.USER)
    suspend fun registerOrLogin(
        @Body body: RegisterOrLoginRequest,
    ): Response<AuthResponse>

    @GET(TrendingNowApiPaths.USER)
    suspend fun getCurrentUser(): Response<UserResponse>

    @PATCH(TrendingNowApiPaths.USER)
    suspend fun updateUser(
        @Body body: UpdateUserRequest,
    ): Response<UserResponse>
}
