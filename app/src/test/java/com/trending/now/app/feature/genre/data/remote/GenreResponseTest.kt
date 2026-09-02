package com.trending.now.app.feature.genre.data.remote

import org.junit.Assert.assertEquals
import org.junit.Test

class GenreResponseTest {
    @Test
    fun mappingDropsInvalidIdsAndDeduplicatesCreatorsWithinGenre() {
        val creator = GenreCreatorResponse(
            id = "creator-1",
            name = "Samay_Raina",
            image = "image",
            role = "Stand-up Comedian",
        )
        val response = GenreResponse(
            success = true,
            message = null,
            data = listOf(
                GenreDataResponse(
                    id = "genre-1",
                    genreName = "Comedy",
                    genreColor = "#FF8533",
                    genreImage = null,
                    genreLogo = "logo",
                    creatorsList = listOf(
                        creator,
                        creator,
                        GenreCreatorResponse(
                            id = null,
                            name = "Invalid",
                            image = null,
                            role = null,
                        ),
                    ),
                ),
                GenreDataResponse(
                    id = null,
                    genreName = "Invalid genre",
                    genreColor = null,
                    genreImage = null,
                    genreLogo = null,
                    creatorsList = emptyList(),
                ),
            ),
        )

        val genres = response.toGenres()

        assertEquals(1, genres.size)
        assertEquals("Comedy", genres.single().name)
        assertEquals(1, genres.single().creators.size)
        assertEquals("creator-1", genres.single().creators.single().id)
    }
}
