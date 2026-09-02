package com.trending.now.app.feature.genre.data.remote

import com.trending.now.app.core.constants.TrendingNowApiPaths
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface GenreApiService {
    @GET(TrendingNowApiPaths.GENRES)
    suspend fun getGenres(): Response<GenreResponse>

    @POST(TrendingNowApiPaths.GENRES)
    suspend fun addGenre(
        @Body body: GenreRequest,
    ): Response<ResponseBody>

    @PUT(TrendingNowApiPaths.GENRE_BY_ID)
    suspend fun updateGenre(
        @Path("id") id: String,
        @Body body: GenreRequest,
    ): Response<ResponseBody>

    @DELETE(TrendingNowApiPaths.GENRE_BY_ID)
    suspend fun deleteGenre(
        @Path("id") id: String,
    ): Response<ResponseBody>

    @PATCH(TrendingNowApiPaths.GENRE_CREATORS)
    suspend fun addCreatorsToGenre(
        @Path("id") id: String,
        @Body body: GenreCreatorsRequest,
    ): Response<ResponseBody>

    @DELETE(TrendingNowApiPaths.GENRE_CREATOR)
    suspend fun removeCreatorFromGenre(
        @Path("id") id: String,
        @Path("creator") creator: String,
    ): Response<ResponseBody>
}
