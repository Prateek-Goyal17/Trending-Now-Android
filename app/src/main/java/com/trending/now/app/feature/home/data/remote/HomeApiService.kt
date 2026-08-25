package com.trending.now.app.feature.home.data.remote

import com.trending.now.app.core.constants.TrendingNowApiPaths
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface HomeApiService {
    @GET(TrendingNowApiPaths.HOMEPAGE_FEED)
    suspend fun getHomepageFeed(): Response<ResponseBody>
}
