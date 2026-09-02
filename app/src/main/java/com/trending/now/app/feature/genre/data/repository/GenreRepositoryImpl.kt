package com.trending.now.app.feature.genre.data.repository

import android.util.Log
import com.trending.now.app.feature.genre.data.remote.GenreApiService
import com.trending.now.app.feature.genre.data.remote.toGenres
import com.trending.now.app.feature.genre.domain.model.Genre
import com.trending.now.app.feature.genre.domain.repository.GenreRepository
import javax.inject.Inject
import javax.inject.Singleton
import retrofit2.Response

@Singleton
class GenreRepositoryImpl @Inject constructor(
    private val genreApiService: GenreApiService,
) : GenreRepository {
    override suspend fun getGenres(): Result<List<Genre>> {
        return runCatching {
            val response = genreApiService.getGenres()
            val genreResponse = response.requireBody()
            if (!genreResponse.success) {
                error(genreResponse.message ?: "Unable to load genres.")
            }

            genreResponse.toGenres()
        }.onFailure { error ->
            Log.d(TAG, "Genre request failed: ${error::class.java.simpleName}")
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
        const val TAG = "GenreRepository"
    }
}
