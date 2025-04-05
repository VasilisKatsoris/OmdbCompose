package com.omdb.data

import com.omdb.domain.MovieDetails
import com.omdb.domain.MovieDetailsRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class MovieDetailsRepositoryImpl @Inject constructor(
    val movieDetailsApiService: MovieDetailsApiService,
    val mapper: MovieDetailsMapper
): MovieDetailsRepository {
    override suspend fun getMovieDetails(movieId: String): Result<MovieDetails> {
        return apiCall {
            movieDetailsApiService.getMovieDetailsForImdbId(movieId)
        }.mapToResult().map {
            mapper.map(it)
        }
    }
}