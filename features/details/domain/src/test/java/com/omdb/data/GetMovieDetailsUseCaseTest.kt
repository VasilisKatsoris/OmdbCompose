package com.omdb.data

import com.omdb.domain.GetMovieDetailsUseCase
import com.omdb.domain.MovieDetails
import com.omdb.domain.MovieDetailsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

import org.junit.Assert.*

class GetMovieDetailsUseCaseTest {

    private val MOVIE_ID = "1"
    private val repository = mockk<MovieDetailsRepository>()
    val getMovieDetailsUseCase = GetMovieDetailsUseCase(repository)

    private val testMovieDetails = MovieDetails(
        imdbID = "1",
        title = "Test"
    )

    @Test
    fun `get movie details returns success when repository returns success`() = runTest  {
        coEvery { repository.getMovieDetails(MOVIE_ID) } returns Result.success(testMovieDetails)
        val result =  getMovieDetailsUseCase(MOVIE_ID)
        assertTrue(result.isSuccess)
        assertEquals(testMovieDetails, result.getOrNull())
    }
    
    @Test
    fun `get movie details returns error when repository returns error`() = runTest {
        val expectedError = Exception("Network error")
        coEvery { repository.getMovieDetails(MOVIE_ID) } returns Result.failure(expectedError)
        val result = getMovieDetailsUseCase(MOVIE_ID)
        assertTrue(result.isFailure)
        assertEquals(expectedError, result.exceptionOrNull())
    }
    
    @Test
    fun `get movie details passes correct movie id to repository`() = runTest  {
        val mockMovie = mockk<MovieDetails>()
        coEvery { repository.getMovieDetails(MOVIE_ID) } returns Result.success(mockMovie)
        getMovieDetailsUseCase(MOVIE_ID)
        coVerify(exactly = 1) { repository.getMovieDetails(MOVIE_ID) }
    }
}