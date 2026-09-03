package com.trending.now.app.feature.creator.domain.repository

import com.trending.now.app.feature.creator.data.remote.CreatorScreenResponse
import com.trending.now.app.feature.creator.data.remote.CreatorDetailResponse

interface CreatorRepository {
    suspend fun getCreatorScreenFeed(): Result<CreatorScreenResponse>

    suspend fun getCreatorDetail(creatorSlug: String): Result<CreatorDetailResponse>

    suspend fun addFavoriteCreator(creatorId: String): Result<Unit>
}
