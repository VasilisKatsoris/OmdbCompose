package com.omdb.data.di

import com.omdb.data.SearchMoviesRepositoryImpl
import com.omdb.domain.SearchMoviesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class SearchBindModule {
    @Binds
    abstract fun bindSearchRepository(searchRepositoryImpl: SearchMoviesRepositoryImpl): SearchMoviesRepository
}