package com.akeshishi.moviex.pojo.shows

import com.akeshishi.moviex.pojo.search.CombinedMovies
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * The model to get the tv shows list result.
 */
@JsonClass(generateAdapter = true)
data class ShowListResult(
    val page: Int,
    @Json(name = "results")
    val showList: List<CombinedMovies>,
    @Json(name = "total_pages")
    val totalPages: Int,
    @Json(name = "total_results")
    val totalResults: Int
)