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
class MovieDetailsRepositoryImplTest {

    private val mapper: MovieDetailsMapper = MovieDetailsMapper()
    private val apiService = mockk<MovieDetailsApiService>()
    private val repository = MovieDetailsRepositoryImpl(
        movieDetailsApiService = apiService,
        mapper = mapper
    )

    @Test
    fun `when getMovieDetails succeeds, should return movie details`() = runTest {
        // Arrange
        val movieId = "tt1234567"
        val movieDetailsResponse = MovieDetailsResponse(
            title = "Test Movie",
            year = "2023",
        )

        coEvery { apiService.getMovieDetailsForImdbId(movieId) } returns Response.success(movieDetailsResponse)

        val result = repository.getMovieDetails(movieId)

        val expectedMappedMovieDetails = mapper.map(movieDetailsResponse)
        assert(result.isSuccess)
        val movieDetails = result.getOrNull() ?: throw AssertionError()
        assertEquals(expectedMappedMovieDetails, movieDetails)
    }
    
    @Test
    fun `when getMovieDetails fails, should return failure result`() = runTest {
        
        val movieId = "1"
        coEvery { apiService.getMovieDetailsForImdbId(movieId) } returns Response.error(
            HttpURLConnection.HTTP_INTERNAL_ERROR,
            "".toResponseBody(null)
        )

        val result = repository.getMovieDetails(movieId)
        assert(result.isFailure)

    }

}