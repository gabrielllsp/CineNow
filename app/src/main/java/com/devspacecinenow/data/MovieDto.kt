package com.devspacecinenow.data

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class MovieDto(
    val id: Int,
    val title: String,
    val overview: String,
    @SerialName("poster_path")
    val postPath: String
)
