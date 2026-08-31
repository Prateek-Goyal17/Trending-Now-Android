package com.trending.now.app.feature.home.domain.repository

import com.trending.now.app.feature.home.domain.model.HomepageFeed

interface HomeRepository {
    suspend fun getHomepageFeed(): Result<HomepageFeed>
}
