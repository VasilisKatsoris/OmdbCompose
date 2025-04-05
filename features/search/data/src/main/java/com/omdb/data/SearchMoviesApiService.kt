package com.omdb.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchMoviesApiService {
    @GET("./")
    suspend fun searchMoviesFor(
        @Query("s") s:String,
        @Query("page") page: Int
    ): Response<MoviesListResponseDao>
}