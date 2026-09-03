package com.trending.now.app.feature.home.data.remote

import com.google.gson.annotations.SerializedName
import com.trending.now.app.core.data.remote.SocialPostMediaResponse
import com.trending.now.app.core.data.remote.SocialPostResponse
import com.trending.now.app.feature.home.domain.model.CreatorSummary
import com.trending.now.app.feature.home.domain.model.HomepageFeed
import com.trending.now.app.feature.home.domain.model.HomepagePost
import com.trending.now.app.feature.home.domain.model.HomepagePostMedia
import com.trending.now.app.feature.home.domain.model.HomepagePostStackItem
import com.trending.now.app.feature.home.domain.model.HomepageTopHeadline

data class HomepageFeedResponse(
    val success: Boolean,
    val message: String?,
    val data: HomepageFeedDataResponse?,
)

data class HomepageFeedDataResponse(
    val trendingCreator: List<TrendingCreatorResponse>?,
)

data class TrendingCreatorResponse(
    val creatorSlug: CreatorSlugResponse?,
    val topHeadline: TopHeadlineResponse?,
    val topicSlug: List<String>?,
    @SerializedName("PostStack")
    val postStack: List<PostStackResponse>?,
)

data class CreatorSlugResponse(
    val name: String?,
    val trendingScore: String?,
    val image: String?,
    val accentColor: String?,
)

data class TopHeadlineResponse(
    @SerializedName("_id")
    val id: String?,
    val headline: String?,
)

data class PostStackResponse(
    val creatorSlug: String?,
    val topicSlug: String?,
    val topicLabel: String?,
    val topicCount: Int?,
    val igPost: SocialPostResponse?,
    val twPost: SocialPostResponse?,
    val shortPost: SocialPostResponse?,
    val headline: String?,
    val feedbackQuestion: String?,
)

fun HomepageFeedResponse.toHomepageFeed(): HomepageFeed {
    return HomepageFeed(
        trendingCreators = data?.trendingCreator.orEmpty().map { it.toCreatorSummary() },
    )
}

private fun TrendingCreatorResponse.toCreatorSummary(): CreatorSummary {
    return CreatorSummary(
        name = creatorSlug?.name.orEmpty(),
        trendingScore = creatorSlug?.trendingScore,
        image = creatorSlug?.image,
        accentColor = creatorSlug?.accentColor,
        topHeadline = topHeadline?.toHomepageTopHeadline(),
        topicSlugs = topicSlug.orEmpty(),
        postStack = postStack.orEmpty().map { it.toHomepagePostStackItem() },
    )
}

private fun TopHeadlineResponse.toHomepageTopHeadline(): HomepageTopHeadline {
    return HomepageTopHeadline(
        id = id.orEmpty(),
        headline = headline.orEmpty(),
    )
}

private fun PostStackResponse.toHomepagePostStackItem(): HomepagePostStackItem {
    return HomepagePostStackItem(
        creatorSlug = creatorSlug.orEmpty(),
        topicSlug = topicSlug.orEmpty(),
        topicLabel = topicLabel.orEmpty(),
        topicCount = topicCount ?: 0,
        instagramPost = igPost?.toHomepagePost(),
        twitterPost = twPost?.toHomepagePost(),
        shortPost = shortPost?.toHomepagePost(),
        headline = headline.orEmpty(),
        feedbackQuestion = feedbackQuestion.orEmpty(),
    )
}

private fun SocialPostResponse.toHomepagePost(): HomepagePost {
    return HomepagePost(
        id = id.orEmpty(),
        platform = platform.orEmpty(),
        account = account.orEmpty(),
        url = url.orEmpty(),
        caption = caption.orEmpty(),
        hashtags = hashtags.orEmpty(),
        media = media.orEmpty().map { it.toHomepagePostMedia() },
        likeCount = likeCount.toIntOrZero(),
        commentCount = commentCount.toIntOrZero(),
        publishedAt = publishedAt,
        category = category,
    )
}

private fun SocialPostMediaResponse.toHomepagePostMedia(): HomepagePostMedia {
    return HomepagePostMedia(
        type = type.orEmpty(),
        url = url ?: imageUrl ?: videoUrl.orEmpty(),
        poster = poster ?: posterUrl,
    )
}

private fun Long?.toIntOrZero(): Int {
    return this?.coerceAtMost(Int.MAX_VALUE.toLong())?.toInt() ?: 0
}
