package com.omdb.data

import androidx.paging.PagingSource
import com.omdb.core.framework.AppConstants
import com.omdb.domain.Movie
import com.omdb.domain.SearchMoviesUseCase
import com.omdb.presentation.SearchPagingSourceFactory
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchPagingSourceFactoryTest {

    val searchMoviesUseCase: SearchMoviesUseCase = mockk()

    private val factory = SearchPagingSourceFactory(searchMoviesUseCase)

    @Test
    fun `PagingSource load returns Success with data when useCase returns results`() = runTest {
        // Given
        val query = "test"
        val page = 1
        val movieResults = createListOfMovies(2)

        coEvery { searchMoviesUseCase(any(), any()) } returns Result.success(movieResults)

        val pagingSource = factory.create(query)
        
        val loadResult = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = page, loadSize = 10, placeholdersEnabled = false)
        )

        assertTrue(loadResult is PagingSource.LoadResult.Page)
        assertEquals(movieResults, (loadResult as PagingSource.LoadResult.Page).data)
    }

    @Test
    fun `PagingSource load returns empty result if query is too short`() = runTest {
        //if query is shorter than AppConstants.SEARCH_MIN_CHAR_LENGTH result should be empty
        val query = "te"
        val loadResult = loadResultForMoviesListOfSize(5, query)
        assertTrue(loadResult is PagingSource.LoadResult.Page)
        assert((loadResult as PagingSource.LoadResult.Page).data.isEmpty())
    }

    @Test
    fun `PagingSource load returns empty result when use case returns empty result`() = runTest {
        val loadResult = loadResultForMoviesListOfSize(0)
        assertTrue(loadResult is PagingSource.LoadResult.Page)
        assert((loadResult as PagingSource.LoadResult.Page).data.isEmpty())
    }

    @Test
    fun `PagingSource load returns Error when useCase returns failure`() = runTest {
        val query = "test"
        val page = 1
        val error = RuntimeException("Network error")

        coEvery { searchMoviesUseCase(any(), any()) } returns Result.failure(error)

        val pagingSource = factory.create(query)
        val loadResult = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = page, loadSize = 10, placeholdersEnabled = false)
        )

        assertTrue(loadResult is PagingSource.LoadResult.Error)
        assertEquals(error, (loadResult as PagingSource.LoadResult.Error).throwable)
    }

    @Test
    fun `Paging ends when results are less than page size`() = runTest {
        val loadResult = loadResultForMoviesListOfSize(5)
        assertTrue(loadResult is PagingSource.LoadResult.Page)
        assert((loadResult as PagingSource.LoadResult.Page).nextKey == null)
    }


    @Test
    fun `Paging continues when results are not less than load size`() = runTest {
        val loadResult = loadResultForMoviesListOfSize(10)
        assertTrue(loadResult is PagingSource.LoadResult.Page)
        assert((loadResult as PagingSource.LoadResult.Page).nextKey != null)
    }

    private fun createListOfMovies(size: Int) = List(size) {
        i -> Movie(imdbID = i.toString(), title = "Movie Title $i", year = "", poster = "")
    }

    private suspend fun loadResultForMoviesListOfSize(
        movesListSize: Int,
        query: String = "test",
    ): PagingSource.LoadResult<Int, Movie> {
        val moviesList = createListOfMovies(movesListSize)
        coEvery { searchMoviesUseCase(any(), any()) } returns Result.success(moviesList)

        val pagingSource = factory.create(query)

        val loadResult = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = 1, loadSize = AppConstants.PAGE_SIZE, placeholdersEnabled = false)
        )
        return loadResult
    }


}