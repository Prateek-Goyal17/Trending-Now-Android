package com.trending.now.app.core.navigation

import android.net.Uri

object AppRoute {
    const val CREATOR_SLUG = "creatorSlug"
    const val GENRE_ID = "genreId"
    const val WEB_VIEW_TITLE = "title"
    const val WEB_VIEW_URL = "url"

    const val LOGIN = "login"
    const val HOME = "home"
    const val CREATORS = "creators"
    const val CREATOR_SEARCH = "creator_search"
    const val GENRE_CREATORS = "genre_creators/{$GENRE_ID}"
    const val PICK_FAVORITE_CREATORS = "pick_favorite_creators"
    const val CREATOR_VIDEO_FEED = "creator_video_feed"
    const val CREATOR_DETAIL = "creator_detail/{$CREATOR_SLUG}"
    const val ME = "me"
    const val FOLLOWING = "following"
    const val SAVED = "saved"
    const val MY_ACTIVITY = "my_activity"
    const val TIME_SPENT = "time_spent"
    const val REPORT_PROBLEM = "report_problem"
    const val WEB_VIEW = "web_view/{$WEB_VIEW_TITLE}/{$WEB_VIEW_URL}"
    const val PRIVACY_POLICY_URL = "https://example.com"
    const val COMMUNITY_GUIDELINE_URL = "https://example.com"

    fun webView(
        title: String,
        url: String,
    ): String = "web_view/${Uri.encode(title)}/${Uri.encode(url)}"

    fun creatorDetail(
        creatorSlug: String,
    ): String = "creator_detail/${Uri.encode(creatorSlug)}"

    fun genreCreators(
        genreId: String,
    ): String = "genre_creators/${Uri.encode(genreId)}"
}
