package com.omdb.presentation

sealed class MovieDetailsIntent {
    data object Retry: MovieDetailsIntent()
}