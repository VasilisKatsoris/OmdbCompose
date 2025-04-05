package com.omdb.data

import org.junit.Assert.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class MovieMapperTest {

    private val mapper = MovieMapper()

    private fun generateRandomString(length: Int = 10): String {
        val charPool = ('a'..'z') + ('A'..'Z') + ('0'..'9') + listOf(' ', '_', '-')
        return (1..length).map { charPool.random() }.joinToString("")
    }

    private fun randomMovieResponse(): MovieResponse {
        return MovieResponse(
            title = generateRandomString(),
            year = generateRandomString(),
            poster = generateRandomString(),
            imdbID = generateRandomString(),
            type = generateRandomString(),
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
                Pair(response.poster, result.poster),
            )
            list.forEach {
                assertEquals(it.first, it.second)
            }
        }
    }

    @Test
    fun `test null imdbId`() {
        val movieResponse = MovieResponse(imdbID = null)

        assertThrows(IllegalArgumentException::class.java) {
            mapper.map(movieResponse)
        }
    }

}