package com.omdb.core.framework.di

import com.omdb.core.framework.DispatchersProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
class FrameworkModule {
    
    @Provides
    fun provideDispatchers(): DispatchersProvider {
        return object: DispatchersProvider {
            override val main = Dispatchers.Main
            override val io = Dispatchers.Main
            override val default = Dispatchers.Main
        }
    }
}