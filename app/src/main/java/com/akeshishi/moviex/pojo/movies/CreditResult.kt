package com.akeshishi.moviex.pojo.movies

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * The model to get the movie's credit list result.
 *
 * @property id is the movie's unique id.
 */
@JsonClass(generateAdapter = true)
data class CreditResult(
    val cast: List<CreditDetails>,
    val crew: List<CreditDetails>,
    val id: Int
)

/**
 * The model to get the movie's credit list details' result.
 */
@JsonClass(generateAdapter = true)
data class CreditDetails(
    val id: Int,
    val name: String,
    val character: String?,
    val gender: Int,
    @Json(name = "profile_path")
    val profilePath: String?,
    @Json(name = "known_for_department")
    val knownForDepartment: String?,
    val department: String?,
    val job: String?
)