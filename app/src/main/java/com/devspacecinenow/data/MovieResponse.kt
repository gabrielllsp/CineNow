package com.devspacecinenow.data

@kotlinx.serialization.Serializable
data class MovieResponse(
    val results: List<MovieDto>
)
