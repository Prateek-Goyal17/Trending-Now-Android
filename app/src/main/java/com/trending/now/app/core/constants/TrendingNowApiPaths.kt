package com.trending.now.app.core.constants

object TrendingNowApiPaths {
    const val USER = "api/user/"
    const val USER_BY_UID = "api/user/{uid}/"
    const val FAVORITE_CREATORS = "api/user/favorite-creators/"
    const val GENRES = "api/genre/"
    const val GENRE_BY_ID = "api/genre/{id}/"
    const val GENRE_CREATORS = "api/genre/{id}/creators/"
    const val GENRE_CREATOR = "api/genre/{id}/creators/{creator}/"
    const val HOMEPAGE_FEED = "api/feed/homepage/"
    const val CREATOR_PAGE = "api/creator/{creator}/"
    const val CREATOR_RANK = "api/rank/"
    const val COMMENTS_BY_POST = "api/user/comment/{postId}/"
    const val COMMENTS = "api/user/comment/"
}
