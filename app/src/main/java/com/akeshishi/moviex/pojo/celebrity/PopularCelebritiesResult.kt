package com.akeshishi.moviex.pojo.celebrity

import com.akeshishi.moviex.pojo.search.KnownFor
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * The model to get the popular celebrities list result.
 */
@JsonClass(generateAdapter = true)
data class PopularCelebritiesResult(
    val page: Int,
    val results: List<PopularCelebrities>,
    @Json(name = "total_pages")
    val totalPages: Int,
    @Json(name = "total_results")
    val totalResults: Int
)

/**
 * The model to get the popular celebrity's details' result.
 */
@JsonClass(generateAdapter = true)
data class PopularCelebrities(
    val adult: Boolean,
    val gender: Int,
    val id: Int,
    @Json(name = "known_for_department")
    val knownForDepartment: String,
    @Json(name = "known_for")
    val knownFor: List<KnownFor>,
    val name: String,
    val popularity: Double,
    @Json(name = "profile_path")
    val profilePath: String?
)