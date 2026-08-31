package com.trending.now.app.feature.home.data.repository

import android.util.Log
import com.trending.now.app.feature.home.data.remote.HomeApiService
import com.trending.now.app.feature.home.data.remote.toHomepageFeed
import com.trending.now.app.feature.home.domain.model.HomepageFeed
import com.trending.now.app.feature.home.domain.repository.HomeRepository
import javax.inject.Inject
import javax.inject.Singleton
import retrofit2.Response

@Singleton
class HomeRepositoryImpl @Inject constructor(
    private val homeApiService: HomeApiService,
) : HomeRepository {
    override suspend fun getHomepageFeed(): Result<HomepageFeed> {
        return runCatching {
            val response = homeApiService.getHomepageFeed()
            val feedResponse = response.requireBody()
            if (!feedResponse.success) {
                error(feedResponse.message ?: "Homepage feed request failed.")
            }

            feedResponse.toHomepageFeed()
        }.onFailure { error ->
            Log.d(TAG, "Homepage feed failed: ${error::class.java.simpleName}")
        }
    }

    private fun <T> Response<T>.requireBody(): T {
        if (!isSuccessful) {
            error("Request failed with code ${code()}.")
        }

        return requireNotNull(body()) {
            "Response body is empty."
        }
    }

    private companion object {
        const val TAG = "HomeRepository"
    }
}
