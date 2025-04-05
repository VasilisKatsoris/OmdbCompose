package com.omdb

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class OmdbApp: Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.tag("OmdbApp")
        Timber.plant(Timber.DebugTree())
    }
}