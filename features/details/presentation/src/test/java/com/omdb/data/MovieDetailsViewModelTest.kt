package com.omdb.data

import app.cash.turbine.test
import com.omdb.core.framework.DispatchersProvider
import com.omdb.domain.GetMovieDetailsUseCase
import com.omdb.domain.Movie
import com.omdb.domain.MovieDetails
import com.omdb.presentation.MovieDetailsState
import com.omdb.presentation.MovieDetailsViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class MovieDetailsViewModelTest {

    val testDispatcher = StandardTestDispatcher()
    private val dispatchersProvider = object : DispatchersProvider {
        override val main = testDispatcher
        override val io = testDispatcher
        override val default = testDispatcher
    }

    private val getMovieDetailsUseCase: GetMovieDetailsUseCase = mockk()

    private val testMovie = Movie(
        imdbID = "1",
        title = "Movie Title",
        year = "2023",
        poster = "https://example.com/poster.jpg",
    )

    private fun createViewModel() = MovieDetailsViewModel(
        movie = testMovie,
        dispatchersProvider = dispatchersProvider,
        getMovieDetailsUseCase = getMovieDetailsUseCase
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(dispatchersProvider.main)
    }

    @Test
    fun `test movie details success`() = runTest {
        val testMovieDetails = MovieDetails(
            imdbID = testMovie.imdbID,
            title = testMovie.title,
            year = testMovie.year,
            poster = testMovie.poster,
        )

        coEvery { getMovieDetailsUseCase(any()) } returns Result.success(testMovieDetails)

        val viewModel = createViewModel()

        viewModel.viewState.test {
            assert(awaitItem() is MovieDetailsState.Loading)
            assertEquals(MovieDetailsState.Success(testMovieDetails), awaitItem())
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `test movie details failure`() = runTest {

        coEvery { getMovieDetailsUseCase(any()) } returns Result.failure(Exception("Error"))

        val viewModel = createViewModel()

        viewModel.viewState.test {
            assert(awaitItem() is MovieDetailsState.Loading)
            assert(awaitItem() is MovieDetailsState.Error)
            ensureAllEventsConsumed()
        }
    }
 
}