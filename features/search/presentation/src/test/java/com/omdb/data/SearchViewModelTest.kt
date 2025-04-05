package com.omdb.data

import androidx.paging.testing.asPagingSourceFactory
import androidx.paging.testing.asSnapshot
import app.cash.turbine.test
import com.omdb.core.framework.AppConstants
import com.omdb.core.framework.DispatchersProvider
import com.omdb.domain.Movie
import com.omdb.domain.SearchMoviesUseCase
import com.omdb.presentation.HomeIntent
import com.omdb.presentation.SearchMoviesViewModel
import com.omdb.presentation.SearchPagingSourceFactory
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class SearchViewModelTest {

    val testDispatcher = StandardTestDispatcher()
    private val dispatchersProvider = object : DispatchersProvider {
        override val main = testDispatcher
        override val io = testDispatcher
        override val default = testDispatcher
    }

    private val searchPagingSourceFactory: SearchPagingSourceFactory = mockk()

    private fun createViewModel() = SearchMoviesViewModel(
        dispatchersProvider = dispatchersProvider,
        pagingSourceFactory = searchPagingSourceFactory
    )

    private fun createListOfMovies(size: Int) = List(size) {
        i -> Movie(imdbID = i.toString(), title = "Movie Title $i", year = "", poster = "")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(dispatchersProvider.main)
    }

    @Test
    fun `test new pager created at every type event`() = runTest {
        coEvery { searchPagingSourceFactory.create(any()) }  returns createListOfMovies(0).asPagingSourceFactory()()
        val viewModel = createViewModel()
        viewModel.pagingData.test {
            awaitItem()
            testDispatcher.scheduler.advanceTimeBy(AppConstants.SEARCH_TYPING_DELAY)
            viewModel.handleIntent(HomeIntent.SearchTextTyped("1"))
            awaitItem()
            testDispatcher.scheduler.advanceTimeBy(AppConstants.SEARCH_TYPING_DELAY)
            viewModel.handleIntent(HomeIntent.SearchTextTyped("2"))
            awaitItem()
            coVerify(exactly = 3) { searchPagingSourceFactory.create(any()) }
        }
    }


    @Test
    fun `view model paging data match data returned by pager`() = runTest {
        val expectedMovies = createListOfMovies(2)

        val pagingSourceFactory = expectedMovies.asPagingSourceFactory()

        coEvery { searchPagingSourceFactory.create(any()) } returns pagingSourceFactory()

        val viewModel = createViewModel()

        assertEquals(expectedMovies, viewModel.pagingData.asSnapshot())
    }

    @Test
    fun `view model paging data match data returned by pager, empty data`() = runTest {
        val expectedMovies = createListOfMovies(0)
        val emptyPagingSourceFactory = expectedMovies.asPagingSourceFactory()
        coEvery { searchPagingSourceFactory.create(any()) } returns emptyPagingSourceFactory()
        val viewModel = createViewModel()
        viewModel.handleIntent(HomeIntent.SearchTextTyped("something"))
        val snapshot = viewModel.pagingData.asSnapshot()
        assertTrue(snapshot.isEmpty())
    }

}