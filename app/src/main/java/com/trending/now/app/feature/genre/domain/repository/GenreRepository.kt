package com.trending.now.app.feature.genre.domain.repository

import com.trending.now.app.feature.genre.domain.model.Genre

interface GenreRepository {
    suspend fun getGenres(): Result<List<Genre>>
}
