package com.omdb.domain

import javax.inject.Inject

class SearchMoviesUseCase @Inject constructor(
    private val moviesRepository: SearchMoviesRepository
) {

    suspend operator fun invoke(movieId: String, page: Int) =
        moviesRepository.searchForMovies(movieId, page)
}
