package com.akeshishi.moviex.pojo.movies

import com.akeshishi.moviex.pojo.search.CombinedMovies
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * The model to get the movies list result.
 */
@JsonClass(generateAdapter = true)
data class MovieListResult (
    val page: Int,
    @Json(name = "results")
    val movieList: List<CombinedMovies>,
    @Json(name = "total_pages")
    val totalPages: Int,
    @Json(name = "total_results")
    val totalResults: Int
)

/**
 * The model to get the list of movie or tv show genres' results.
 */
@JsonClass(generateAdapter = true)
data class GenresResult(
    val genres: List<Genre>
)