package com.trending.now.app.feature.creator.data.repository

import android.util.Log
import com.trending.now.app.feature.creator.data.remote.CreatorApiService
import com.trending.now.app.feature.creator.data.remote.CreatorDetailResponse
import com.trending.now.app.feature.creator.data.remote.FavoriteCreatorRequest
import com.trending.now.app.feature.creator.data.remote.CreatorScreenResponse
import com.trending.now.app.feature.creator.domain.repository.CreatorRepository
import javax.inject.Inject
import javax.inject.Singleton
import retrofit2.Response

@Singleton
class CreatorRepositoryImpl @Inject constructor(
    private val creatorApiService: CreatorApiService,
) : CreatorRepository {
    override suspend fun getCreatorScreenFeed(): Result<CreatorScreenResponse> {
        return runCatching {
            val response = creatorApiService.getCreatorScreenFeed()
            val creatorScreenResponse = response.requireBody()
            if (!creatorScreenResponse.success) {
                error("Creator screen feed request failed.")
            }

            creatorScreenResponse
        }.onFailure { error ->
            Log.d(TAG, "Creator screen feed failed: ${error::class.java.simpleName}")
        }
    }

    override suspend fun getCreatorDetail(creatorSlug: String): Result<CreatorDetailResponse> {
        return runCatching {
            val response = creatorApiService.getCreatorPage(creator = creatorSlug)
            val creatorDetailResponse = response.requireBody()
            if (!creatorDetailResponse.success) {
                error("Creator detail request failed.")
            }

            creatorDetailResponse
        }.onFailure { error ->
            Log.d(TAG, "Creator detail failed: ${error::class.java.simpleName}")
        }
    }

    override suspend fun addFavoriteCreator(creatorId: String): Result<Unit> {
        return runCatching {
            val response = creatorApiService.addFavoriteCreator(
                body = FavoriteCreatorRequest(creatorId = creatorId),
            )
            if (!response.isSuccessful) {
                error("Request failed with code ${response.code()}.")
            }

            response.body()?.close()
            Unit
        }.onFailure { error ->
            Log.d(TAG, "Add favorite creator failed: ${error::class.java.simpleName}")
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
        const val TAG = "CreatorRepository"
    }
}
