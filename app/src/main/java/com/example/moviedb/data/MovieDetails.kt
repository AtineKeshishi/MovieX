package com.example.moviedb.data

import com.google.gson.annotations.SerializedName

data class MovieDetails(
    val id: Int,
    @SerializedName("poster_path")
    val posterPath: String,
    val title: String,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("vote_average")
    val rating: Double,
    val runtime: Int,
    val budget: Int,
    val overview: String
)