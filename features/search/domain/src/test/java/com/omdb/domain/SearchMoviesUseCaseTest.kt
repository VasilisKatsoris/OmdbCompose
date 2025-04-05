package com.omdb.domain

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

import org.junit.Assert.*

class SearchMoviesUseCaseTest {

    private val repository = mockk<SearchMoviesRepository>()
    val searchMoviesUseCase = SearchMoviesUseCase(repository)

    private fun createListOfMovies(size: Int) = List(size) {
        i -> Movie(imdbID = i.toString(), title = "Movie Title $i", year = "", poster = "")
    }

    @Test
    fun `get movie details returns success when repository returns success`() = runTest  {
        val moviesList = createListOfMovies(5)
        coEvery { repository.searchForMovies(any(), any()) } returns Result.success(moviesList)
        val result = searchMoviesUseCase("test", 1)
        assertTrue(result.isSuccess)
        assertEquals(moviesList, result.getOrNull())
    }
    
    @Test
    fun `get movie details returns error when repository returns error`() = runTest {
        val expectedError = Exception("Network error")
        coEvery { repository.searchForMovies(any(), any()) } returns Result.failure(expectedError)
        val result = searchMoviesUseCase("test", 1)
        assertTrue(result.isFailure)
        assertEquals(expectedError, result.exceptionOrNull())
    }

}