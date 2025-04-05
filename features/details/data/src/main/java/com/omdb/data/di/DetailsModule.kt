package com.omdb.data.di

import com.omdb.data.MovieDetailsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DetailsModule {

    @Provides
    @Singleton
    fun provideSearchService(retrofitBuilder: Retrofit.Builder): MovieDetailsApiService {
        return retrofitBuilder
            .build()
            .create(MovieDetailsApiService::class.java)
    }

}