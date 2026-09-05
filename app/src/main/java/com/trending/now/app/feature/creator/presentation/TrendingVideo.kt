package com.trending.now.app.feature.creator.presentation

import com.trending.now.app.core.data.remote.SocialPostResponse
import com.trending.now.app.feature.creator.data.remote.CreatorScreenResponse
import com.trending.now.app.feature.creator.data.remote.trendingNowPosts

data class TrendingVideo(
    val id: String,
    val videoUrl: String,
    val posterUrl: String?,
    val username: String,
    val creatorName: String,
    val platform: String,
    val title: String,
    val description: String,
    val commentCount: Long,
)

fun CreatorScreenResponse.trendingVideos(): List<TrendingVideo> =
    trendingNowPosts().mapNotNull { it.toTrendingVideo() }.distinctBy(TrendingVideo::id)

internal fun SocialPostResponse.toTrendingVideo(): TrendingVideo? {
    val video = media.orEmpty().firstOrNull {
        !it.videoUrl.isNullOrBlank() || (it.type == "video" && !it.url.isNullOrBlank())
    } ?: return null
    val videoUrl = firstNonBlank(video.videoUrl, video.url) ?: return null
    val creatorLabel = firstNonBlank(creatorName, creator, account)
        ?.replace('_', ' ').orEmpty()
    val body = firstNonBlank(caption, text, normalizedText).orEmpty()
        .replace("**", "").trim()
    val heading = firstNonBlank(title, body.lineSequence().firstOrNull(), creatorLabel)
        ?: "Trending video"

    return TrendingVideo(
        id = firstNonBlank(postId, id, videoUrl)!!,
        videoUrl = videoUrl,
        posterUrl = firstNonBlank(video.posterUrl, video.poster, video.imageUrl, thumbnail, thumbnailUrl),
        username = account.orEmpty().removePrefix("@"),
        creatorName = creatorLabel,
        platform = platform.orEmpty(),
        title = heading.replace("**", ""),
        description = firstNonBlank(description)
            ?: if (title.isNullOrBlank()) body.substringAfter('\n', "").trim() else body,
        commentCount = (commentCount ?: 0L).coerceAtLeast(0L),
    )
}

internal fun initialVideoPage(videos: List<TrendingVideo>, postId: String, index: Int): Int {
    val matchingIndex = videos.indexOfFirst { it.id == postId }
    return if (matchingIndex >= 0) matchingIndex else index.coerceIn(0, (videos.size - 1).coerceAtLeast(0))
}

private fun firstNonBlank(vararg values: String?): String? =
    values.firstOrNull { !it.isNullOrBlank() }?.trim()
