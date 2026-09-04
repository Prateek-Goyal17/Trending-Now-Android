package com.trending.now.app.feature.genre.data.remote

import com.google.gson.annotations.SerializedName
import com.trending.now.app.feature.genre.domain.model.Genre
import com.trending.now.app.feature.genre.domain.model.GenreCreator

data class GenreResponse(
    val success: Boolean,
    val message: String?,
    val data: List<GenreDataResponse>?,
)

data class GenreDataResponse(
    @SerializedName("_id")
    val id: String?,
    val genreName: String?,
    val genreColor: String?,
    val genreImage: String?,
    val genreLogo: String?,
    val creatorsList: List<GenreCreatorResponse>?,
)

data class GenreCreatorResponse(
    @SerializedName("_id")
    val id: String?,
    val name: String?,
    val image: String?,
    val role: String?,
    val cardImage: String? = null,
    val stats: GenreCreatorStatsResponse? = null,
)

data class GenreCreatorStatsResponse(
    val instaFCount: Long?,
    val youtubeFCount: String?,
)

fun GenreResponse.toGenres(): List<Genre> {
    return data.orEmpty().mapNotNull { genre ->
        val genreId = genre.id?.takeIf(String::isNotBlank) ?: return@mapNotNull null

        Genre(
            id = genreId,
            name = genre.genreName.orEmpty(),
            color = genre.genreColor,
            imageUrl = genre.genreImage,
            logoUrl = genre.genreLogo,
            creators = genre.creatorsList.orEmpty().mapNotNull { creator ->
                val creatorId = creator.id?.takeIf(String::isNotBlank)
                    ?: return@mapNotNull null

                GenreCreator(
                    id = creatorId,
                    name = creator.name.orEmpty(),
                    imageUrl = creator.image,
                    role = creator.role.orEmpty(),
                    cardImageUrl = creator.cardImage,
                    instagramFollowers = creator.stats?.instaFCount,
                    youtubeFollowers = creator.stats?.youtubeFCount,
                )
            }.distinctBy(GenreCreator::id),
        )
    }
}
