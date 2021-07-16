package com.akeshishi.moviex.pojo.movies

import com.akeshishi.moviex.pojo.shows.ProductionCompany
import com.akeshishi.moviex.pojo.shows.ProductionCountry
import com.akeshishi.moviex.pojo.shows.SpokenLanguage
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * The model to get the movie details' result.
 */
@JsonClass(generateAdapter = true)
data class MovieDetails (
    val id: Int,
    val title: String,
    val genres: List<Genre>,
    val budget: Int,
    val overview: String,
    @Json(name = "release_date")
    val releaseDate: String?,
    @Json(name = "vote_average")
    val rating: Double,
    val runtime: Int,
    val revenue: Int,
    val status: String,
    val tagline: String,
    val homepage: String?,
    @Json(name = "backdrop_path")
    val backdropPath: String?,
    @Json(name = "poster_path")
    val posterPath: String?,
    @Json(name = "production_companies")
    val productionCompanies: List<ProductionCompany>,
    @Json(name = "production_countries")
    val productionCountries: List<ProductionCountry>,
    @Json(name = "spoken_languages")
    val spokenLanguages: List<SpokenLanguage>,
    @Json(name = "original_language")
    val originalLanguage: String,
    @Json(name = "original_title")
    val originalTitle: String,
)

/**
 * The model to get the movie or tv show genres' result.
 */
@JsonClass(generateAdapter = true)
data class Genre(
    val id: Int,
    val name: String
)