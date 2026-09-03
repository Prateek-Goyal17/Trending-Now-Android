package com.trending.now.app.core.data.remote

import com.google.gson.annotations.SerializedName

data class SocialPostResponse(
    @SerializedName("_id")
    val articleId: String?,
    val id: String?,
    val postId: String?,
    val shortId: String?,
    val tweetId: String?,
    val creator: String?,
    val creatorName: String?,
    val platform: String?,
    val account: String?,
    val author: String?,
    val ownerId: String?,
    val url: String?,
    val embedUrl: String?,
    val title: String?,
    val description: String?,
    val content: String?,
    val text: String?,
    val caption: String?,
    val normalizedText: String?,
    val hashtags: List<String>?,
    val media: List<SocialPostMediaResponse>?,
    val mediaCount: Int?,
    val thumbnail: String?,
    val thumbnailUrl: String?,
    val urlToImage: String?,
    val avatar: String?,
    val isVideo: Boolean?,
    val isSidecar: Boolean?,
    val likeCount: Long?,
    val commentCount: Long?,
    val likes: Long?,
    val replies: Long?,
    val retweets: Long?,
    val quotes: Long?,
    val views: Long?,
    val bookmarks: Long?,
    val followers: Long?,
    val verified: Boolean?,
    val hasMedia: Boolean?,
    val unixDate: Long?,
    val publishedAt: String?,
    val time: String?,
    val createdAt: String?,
    val updatedAt: String?,
    val category: String?,
    val topic: String?,
    val topicMeta: SocialPostTopicMetaResponse?,
    val source: SocialPostSourceResponse?,
    val reactions: SocialPostReactionsResponse?,
    val isAlsoHappening: Boolean?,
    val isBreaking: Boolean?,
    val trendingScore: Double?,
    @SerializedName("_diversity")
    val isDiversity: Boolean?,
)

data class SocialPostMediaResponse(
    val type: String?,
    val url: String?,
    val imageUrl: String?,
    val videoUrl: String?,
    val poster: String?,
    val posterUrl: String?,
)

data class SocialPostTopicMetaResponse(
    val slug: String?,
    val label: String?,
    val isHashtag: Boolean?,
    val count: Int?,
)

data class SocialPostSourceResponse(
    val id: String?,
    val name: String?,
)

data class SocialPostReactionsResponse(
    val like: Long?,
)
