package com.omdb.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Movie(
    var imdbID: String,
    var poster: String?,
    var title: String?,
    var year: String?
): Parcelable