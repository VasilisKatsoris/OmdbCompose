package com.omdb.domain

import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(
    private val moviesRepository: MovieDetailsRepository,
) {

    suspend operator fun invoke(movieId: String) = moviesRepository.getMovieDetails(movieId)
}
