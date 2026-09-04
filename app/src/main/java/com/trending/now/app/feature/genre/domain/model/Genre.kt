package com.trending.now.app.feature.genre.domain.model

data class Genre(
    val id: String,
    val name: String,
    val color: String?,
    val imageUrl: String?,
    val logoUrl: String?,
    val creators: List<GenreCreator>,
)

data class GenreCreator(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val role: String,
    val cardImageUrl: String? = null,
    val instagramFollowers: Long? = null,
    val youtubeFollowers: String? = null,
)
