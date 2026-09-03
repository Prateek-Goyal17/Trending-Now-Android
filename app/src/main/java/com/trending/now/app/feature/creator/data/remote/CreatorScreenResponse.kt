package com.trending.now.app.feature.creator.data.remote

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.trending.now.app.core.data.remote.SocialPostResponse

data class CreatorScreenResponse(
    val success: Boolean,
    val userType: String?,
    val data: List<CreatorScreenSectionResponse>?,
)

data class CreatorScreenSectionResponse(
    val type: String?,
    val data: JsonElement?,
)

data class FavoriteCreatorCardResponse(
    val title: String?,
    val description: String?,
    val image: String?,
)

data class ExistingFavoriteCreatorResponse(
    @SerializedName("_id")
    val id: String?,
    val name: String?,
    val accentColor: String?,
    val badge: String?,
    val cardImage: String?,
    val createdAt: String?,
    val genres: List<String>?,
    val image: String?,
    val role: String?,
    val socialAccounts: Map<String, String?>?,
    val suggestionImage: String?,
    val trendingScore: Double?,
    val updatedAt: String?,
    val newFetchCount: Int?,
)

data class CreatorSuggestionResponse(
    @SerializedName("CreatorName")
    val creatorName: String?,
    val badge: String?,
    val role: String?,
    @SerializedName("suggestionline")
    val suggestionLine: String?,
    val suggestionImage: String?,
)

data class CreatorBuzzingCardResponse(
    val id: String?,
    val creator: String?,
    val topic: String?,
    val count: Int?,
    val image: String?,
    val type: String?,
    val posts: List<SocialPostResponse>?,
)

val CreatorScreenResponse.isGuest: Boolean
    get() = userType == CreatorScreenUserType.GUEST

val CreatorScreenResponse.isUser: Boolean
    get() = userType == CreatorScreenUserType.USER

fun CreatorScreenResponse.favoriteCreatorCards(): List<FavoriteCreatorCardResponse> {
    return sectionData(CreatorScreenSectionType.FAVORITE_CREATORS)
}

fun CreatorScreenResponse.existingFavoriteCreators(): List<ExistingFavoriteCreatorResponse> {
    return sectionData(CreatorScreenSectionType.FAVORITE_CREATORS)
}

fun CreatorScreenResponse.trendingNowPosts(): List<SocialPostResponse> {
    return sectionData(CreatorScreenSectionType.TRENDING_NOW)
}

fun CreatorScreenResponse.creatorSuggestions(): List<CreatorSuggestionResponse> {
    return sectionData(CreatorScreenSectionType.CREATOR_SUGGESTIONS)
}

fun CreatorScreenResponse.buzzingCards(): List<CreatorBuzzingCardResponse> {
    return sectionData(CreatorScreenSectionType.BUZZING_CARDS)
}

private inline fun <reified T> CreatorScreenResponse.sectionData(
    sectionType: String,
): List<T> {
    return data
        .orEmpty()
        .firstOrNull { section -> section.type == sectionType }
        ?.data
        .toResponseList()
}

private inline fun <reified T> JsonElement?.toResponseList(): List<T> {
    if (this == null || isJsonNull) {
        return emptyList()
    }

    val listType = object : TypeToken<List<T>>() {}.type
    return creatorScreenResponseGson.fromJson(this, listType)
}

object CreatorScreenUserType {
    const val GUEST = "Guest"
    const val USER = "User"
}

object CreatorScreenSectionType {
    const val FAVORITE_CREATORS = "favoriteCreators"
    const val TRENDING_NOW = "trendingNow"
    const val CREATOR_SUGGESTIONS = "creatorSuggestions"
    const val BUZZING_CARDS = "buzzingCards"
}

private val creatorScreenResponseGson = Gson()
