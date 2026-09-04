package com.trending.now.app.feature.creator.data.remote

import com.trending.now.app.core.data.remote.SocialPostResponse

data class CreatorDetailResponse(
    val success: Boolean,
    val data: CreatorDetailDataResponse?,
)

data class CreatorDetailDataResponse(
    val creatorName: String?,
    val role: String?,
    val bannerImage: String?,
    val socialFollows: CreatorSocialFollowsResponse?,
    val createdAt: String?,
    val updatedAt: String?,
    val stats: CreatorDetailStatsResponse?,
    val sections: CreatorDetailSectionsResponse?,
    val categorized: CreatorDetailCategorizedResponse?,
    val dumpInfo: CreatorDetailDumpInfoResponse?,
    val upNext: CreatorDetailUpNextResponse?,
)

data class CreatorSocialFollowsResponse(
    val instaFollowers: Long?,
    val youtubeSubs: String?,
)

data class CreatorDetailStatsResponse(
    val instagram: CreatorDetailPlatformStatsResponse?,
    val youtubeShorts: CreatorDetailPlatformStatsResponse?,
    val twitter: CreatorDetailPlatformStatsResponse?,
    val news: CreatorDetailPlatformStatsResponse?,
)

data class CreatorDetailPlatformStatsResponse(
    val totalDocuments: Int?,
    val totalPosts: Int?,
    val totalShorts: Int?,
    val totalViews: Long?,
    val totalLikes: Long?,
    val totalArticles: Int?,
)

data class CreatorDetailSectionsResponse(
    val instagram: List<SocialPostResponse>?,
    val youtubeShorts: List<SocialPostResponse>?,
    val twitter: List<SocialPostResponse>?,
    val news: List<SocialPostResponse>?,
)

data class CreatorDetailCategorizedResponse(
    val news: List<SocialPostResponse>?,
    val lifestyle: List<SocialPostResponse>?,
)

data class CreatorDetailDumpInfoResponse(
    val totalDumpDocuments: Int?,
    val latestDumpDate: String?,
    val oldestDumpDate: String?,
    val platformCoverage: Map<String, Int>?,
)

data class CreatorDetailUpNextResponse(
    val creatorName: String?,
    val image: String?,
    val slug: String?,
)
