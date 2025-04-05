package com.omdb.ui

import com.omdb.domain.Movie
import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {
    @Serializable
    data object Search : Routes()

    @Serializable
    data class Details(val movie: Movie) : Routes()
}