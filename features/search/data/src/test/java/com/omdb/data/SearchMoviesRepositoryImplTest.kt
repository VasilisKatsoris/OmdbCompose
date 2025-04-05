package com.omdb.data

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test

import org.junit.Assert.*
import retrofit2.Response
import java.net.HttpURLConnection

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class SearchMoviesRepositoryImplTest {

    private val mapper: MovieMapper = MovieMapper()
    private val apiService = mockk<SearchMoviesApiService>()

    private val repository = SearchMoviesRepositoryImpl(
        searchMoviesApiService = apiService,
        mapper = mapper
    )

    private fun createListOfMovieResponse(size: Int) = List(size) {
        i -> MovieResponse(imdbID = i.toString(), title = "Movie Title $i", year = "", poster = "")
    }

    @Test
    fun `when search call succeeds, should return mapped movies list`() = runTest {
        val movieDetailsResponse = createListOfMovieResponse(4)
        coEvery { apiService.searchMoviesFor(any(), any()) } returns Response.success(
            MoviesListResponseDao(
                success = true,
                movieResponses = movieDetailsResponse,
                totalResults = 10
            )
        )

        val result = repository.searchForMovies("test", 1)

        val expectedMappedMovieDetails = movieDetailsResponse.map { mapper.map(it) }
        assert(result.isSuccess)
        val movieDetails = result.getOrNull() ?: throw AssertionError()
        assertEquals(expectedMappedMovieDetails, movieDetails)
    }

    @Test
    fun `when search call fails, should return failure result`() = runTest {
        coEvery { apiService.searchMoviesFor(any(), any()) } returns Response.error(
            HttpURLConnection.HTTP_INTERNAL_ERROR,
            "".toResponseBody(null)
        )

        val result = repository.searchForMovies("test", 1)
        assert(result.isFailure)

    }

}