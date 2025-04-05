package com.omdb.data

import com.omdb.domain.Movie
import javax.inject.Inject

class MovieMapper @Inject constructor() {

    fun map(movieResponse: MovieResponse) = Movie(
        movieResponse.imdbID ?: throw IllegalArgumentException("imdbID is null"),
        movieResponse.poster,
        movieResponse.title,
        movieResponse.year
    )

}