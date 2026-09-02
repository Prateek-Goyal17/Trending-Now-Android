package com.trending.now.app.feature.creator.domain.repository

import com.trending.now.app.feature.creator.data.remote.CreatorScreenResponse

interface CreatorRepository {
    suspend fun getCreatorScreenFeed(): Result<CreatorScreenResponse>

    suspend fun addFavoriteCreator(creatorId: String): Result<Unit>
}
