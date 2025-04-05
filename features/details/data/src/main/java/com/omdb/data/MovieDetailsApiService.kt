package com.omdb.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieDetailsApiService {
    @GET("./")
    suspend fun getMovieDetailsForImdbId(@Query("i") imdbId:String): Response<MovieDetailsResponse>
}