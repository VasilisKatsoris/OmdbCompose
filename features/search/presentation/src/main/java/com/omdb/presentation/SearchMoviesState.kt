package com.omdb.presentation

data class SearchMoviesState(
    val searchText: String = "",
    val inTypeDelayWindow: Boolean = false,
)