package com.trending.now.app.feature.home.domain.model

data class HomepageFeed(
    val trendingCreators: List<CreatorSummary>,
)

data class CreatorSummary(
    val name: String,
    val trendingScore: String?,
    val image: String?,
    val accentColor: String?,
    val topHeadline: HomepageTopHeadline?,
    val topicSlugs: List<String>,
    val postStack: List<HomepagePostStackItem>,
)

data class HomepageTopHeadline(
    val id: String,
    val headline: String,
)

data class HomepagePostStackItem(
    val creatorSlug: String,
    val topicSlug: String,
    val topicLabel: String,
    val topicCount: Int,
    val instagramPost: HomepagePost?,
    val twitterPost: HomepagePost?,
    val shortPost: HomepagePost?,
    val headline: String,
    val feedbackQuestion: String,
)

data class HomepagePost(
    val id: String,
    val platform: String,
    val account: String,
    val url: String,
    val caption: String,
    val hashtags: List<String>,
    val media: List<HomepagePostMedia>,
    val likeCount: Int,
    val commentCount: Int,
    val publishedAt: String?,
    val category: String?,
)

data class HomepagePostMedia(
    val type: String,
    val url: String,
    val poster: String?,
)
