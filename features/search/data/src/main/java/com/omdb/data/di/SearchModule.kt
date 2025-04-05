package com.omdb.data.di

import com.omdb.data.SearchMoviesApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SearchModule {

    @Provides
    @Singleton
    fun provideSearchService(retrofitBuilder: Retrofit.Builder): SearchMoviesApiService {
        return retrofitBuilder
            .build()
            .create(SearchMoviesApiService::class.java)
    }
}