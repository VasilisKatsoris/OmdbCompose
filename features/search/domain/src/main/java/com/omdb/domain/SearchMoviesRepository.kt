package com.omdb.domain


interface SearchMoviesRepository {
    suspend fun searchForMovies(text: String, page: Int): Result<List<Movie>>
}