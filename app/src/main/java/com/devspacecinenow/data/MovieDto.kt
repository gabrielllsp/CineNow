package com.devspacecinenow.data

import com.devspacecinenow.util.Constants.POSTER_PATH
import com.google.gson.annotations.SerializedName


data class MovieDto(
    val id: Int,
    val title: String,
    val overview: String,
    @SerializedName("poster_path")
    val postPath: String
) {
    val posterFullPath: String
        get() = POSTER_PATH + postPath
}
