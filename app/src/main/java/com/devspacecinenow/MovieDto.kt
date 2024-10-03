package com.devspacecinenow

import com.google.gson.annotations.SerializedName

data class MovieDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("poster_path")
    val postPath: String
){
    val posterFullPath: String
        get() = "https://image.tmdb.org/t/p/w300$postPath"
}
