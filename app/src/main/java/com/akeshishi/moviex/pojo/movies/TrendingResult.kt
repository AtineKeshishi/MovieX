package com.akeshishi.moviex.pojo.movies


import com.akeshishi.moviex.pojo.search.CombinedMovies
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * The model to get the trending movies list result.
 */
@JsonClass(generateAdapter = true)
data class TrendingResult(
    val page: Int,
    @Json(name = "results")
    val trendingList: List<CombinedMovies>,
    @Json(name = "total_pages")
    val totalPages: Int,
    @Json(name = "total_results")
    val totalResults: Int
)