package com.omdb.data.di

import com.omdb.data.MovieDetailsRepositoryImpl
import com.omdb.domain.MovieDetailsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class BindModule {
    @Binds
    abstract fun bindDetailsRepository(searchRepositoryImpl: MovieDetailsRepositoryImpl): MovieDetailsRepository
}