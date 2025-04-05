package com.omdb.domain

interface MovieDetailsRepository {
    suspend fun getMovieDetails(movieId: String): Result<MovieDetails>
}