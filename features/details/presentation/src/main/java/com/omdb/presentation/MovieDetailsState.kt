package com.omdb.presentation

import com.omdb.domain.MovieDetails

sealed class MovieDetailsState{
    data object Loading: MovieDetailsState()
    data class Success(val movieDetails: MovieDetails): MovieDetailsState()
    data class Error(val error: String?): MovieDetailsState()
}