package com.omdb.data

import kotlin.test.Test
import kotlin.test.assertEquals

class MovieDetailsMapperTest {

    private val mapper = MovieDetailsMapper()

    private fun generateRandomString(length: Int = 10): String? {
        if (Math.random() < 0.2) return null
        val charPool = ('a'..'z') + ('A'..'Z') + ('0'..'9') + listOf(' ', '_', '-')
        return (1..length).map { charPool.random() }.joinToString("")
    }

    private fun randomMovieResponse(): MovieDetailsResponse {
        return MovieDetailsResponse(
            title = generateRandomString(),
            year = generateRandomString(),
            rated = generateRandomString(),
            released = generateRandomString(),
            runtime = generateRandomString(),
            genre = generateRandomString(),
            director = generateRandomString(),
            writer = generateRandomString(),
            actors = generateRandomString(),
            plot = generateRandomString(),
            language = generateRandomString(),
            country = generateRandomString(),
            awards = generateRandomString(),
            poster = generateRandomString(),
            ratings = listOf(
                RatingRemote(source = generateRandomString(), value = generateRandomString()),
                RatingRemote(source = generateRandomString(), value = generateRandomString()),
                RatingRemote(source = generateRandomString(), value = generateRandomString())
            ),
            metascore = generateRandomString(),
            imdbRating = generateRandomString(),
            imdbVotes = generateRandomString(),
            imdbID = generateRandomString(),
            type = generateRandomString(),
            dVD = generateRandomString(),
            boxOffice = generateRandomString(),
            production = generateRandomString(),
            website = generateRandomString(),
            response = generateRandomString()
        )
    }

    @Test
    fun `map should correctly transform response to domain model`() {
        //try 10 random responses
        for (i in 0..10) {
            val response = randomMovieResponse()
            val result = mapper.map(response)
            val list = listOf(
                Pair(response.imdbID, result.imdbID),
                Pair(response.title, result.title),
                Pair(response.year, result.year),
                Pair(response.rated, result.rated),
                Pair(response.released, result.released),
                Pair(response.runtime, result.runtime),
                Pair(response.genre, result.genre),
                Pair(response.director, result.director),
                Pair(response.writer, result.writer),
                Pair(response.actors, result.actors),
                Pair(response.plot, result.plot),
                Pair(response.language, result.language),
                Pair(response.country, result.country),
                Pair(response.awards, result.awards),
                Pair(response.poster, result.poster),
                Pair(response.metascore, result.metascore),
                Pair(response.imdbRating, result.imdbRating),
                Pair(response.imdbVotes, result.imdbVotes),
                Pair(response.type, result.type),
                Pair(response.dVD, result.dVD),
                Pair(response.boxOffice, result.boxOffice),
                Pair(response.production, result.production),
                Pair(response.website, result.website)
            )
            list.forEach {
                assertEquals(it.first, it.second)
            }
        }
    }


    @Test
    fun `map should correctly transform rating objects`() {
        val remoteRatings = listOf(
            RatingRemote(source = "Source1", value = "Value1"),
            RatingRemote(source = "Source2", value = "Value2")
        )

        val response = MovieDetailsResponse(ratings = remoteRatings)
        val mappedDetails = mapper.map(response)

        assertEquals(2, mappedDetails.ratings?.size)

        for (i in remoteRatings.indices) {
            val remoteRating = remoteRatings[i]
            val mappedRating = mappedDetails.ratings?.get(i)

            assertEquals(remoteRating.source, mappedRating?.source)
            assertEquals(remoteRating.value, mappedRating?.value)
        }

    }

    @Test
    fun `map should handle empty ratings list`() {
        // Given
        val response = MovieDetailsResponse(
            title = "Test Movie",
            imdbID = "tt0000000",
            ratings = emptyList(),
            response = "True"
        )

        // When
        val result = mapper.map(response)

        // Then
        assert(result.ratings?.isEmpty() == true)
    }
}