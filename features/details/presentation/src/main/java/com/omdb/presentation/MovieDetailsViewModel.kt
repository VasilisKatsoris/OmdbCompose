package com.omdb.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omdb.core.framework.DispatchersProvider
import com.omdb.domain.GetMovieDetailsUseCase
import com.omdb.domain.Movie
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel(assistedFactory = MovieDetailsViewModel.Factory::class)
class MovieDetailsViewModel @AssistedInject constructor(
    @Assisted val movie: Movie,
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val dispatchersProvider: DispatchersProvider
): ViewModel() {

    private val _viewState = MutableStateFlow<MovieDetailsState>(MovieDetailsState.Loading)
    val viewState = _viewState.asStateFlow()

    init {
        getMovieDetails()
    }

    private fun getMovieDetails(){
        viewModelScope.launch(dispatchersProvider.io) {
            _viewState.value = MovieDetailsState.Loading
            getMovieDetailsUseCase(movieId = movie.imdbID).fold(
                onSuccess = {
                    _viewState.value = MovieDetailsState.Success(it)
                },
                onFailure = { error ->
                    _viewState.value = MovieDetailsState.Error(error.message)
                    Timber.e(error)
                }
            )
        }
    }


    //entrypoint for ui interactions
    fun handleIntent(intent: MovieDetailsIntent) {
        when (intent) {
            is MovieDetailsIntent.Retry -> getMovieDetails()
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(movie: Movie): MovieDetailsViewModel
    }
}

