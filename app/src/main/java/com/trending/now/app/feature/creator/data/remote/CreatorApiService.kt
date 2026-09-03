package com.trending.now.app.feature.creator.data.remote

import com.trending.now.app.core.constants.TrendingNowApiPaths
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.Path

interface CreatorApiService {
    @GET(TrendingNowApiPaths.CREATOR_SCREEN_FEED)
    suspend fun getCreatorScreenFeed(): Response<CreatorScreenResponse>

    @GET(TrendingNowApiPaths.CREATOR_PAGE)
    suspend fun getCreatorPage(
        @Path("creator") creator: String,
    ): Response<CreatorDetailResponse>

    @GET(TrendingNowApiPaths.CREATOR_RANK)
    suspend fun getCreatorRank(): Response<ResponseBody>

    @GET(TrendingNowApiPaths.FAVORITE_CREATORS)
    suspend fun getFavoriteCreators(): Response<ResponseBody>

    @POST(TrendingNowApiPaths.FAVORITE_CREATORS)
    suspend fun addFavoriteCreator(
        @Body body: FavoriteCreatorRequest,
    ): Response<ResponseBody>

    @HTTP(method = "DELETE", path = TrendingNowApiPaths.FAVORITE_CREATORS, hasBody = true)
    suspend fun removeFavoriteCreator(
        @Body body: FavoriteCreatorRequest,
    ): Response<ResponseBody>
}
