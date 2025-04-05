package com.omdb.data

import com.omdb.domain.Movie
import kotlinx.coroutines.delay
import javax.inject.Inject

class SearchMoviesRepositoryImpl @Inject constructor(
    private val searchMoviesApiService: SearchMoviesApiService,
    private val mapper: MovieMapper
): com.omdb.domain.SearchMoviesRepository {

    override suspend fun searchForMovies(text: String, page: Int): Result<List<Movie>> {
        delay(1000)
        return apiCall {
            searchMoviesApiService.searchMoviesFor(text, page)
        }.mapToResult().map {
            it.movieResponses?.map { mapper.map(it) } ?: emptyList()
        }
    }
}