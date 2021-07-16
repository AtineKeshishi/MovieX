package com.akeshishi.moviex.pojo.celebrity

import com.akeshishi.moviex.pojo.search.CombinedMovies
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * The model to get the celebrity's details' result.
 */
@JsonClass(generateAdapter = true)
data class CelebrityDetails(
    val id: Int,
    val name: String,
    val birthday: String?,
    @Json(name = "place_of_birth")
    val placeOfBirth: String?,
    @Json(name = "known_for_department")
    val knownForDepartment: String?,
    @Json(name = "profile_path")
    val profilePath: String?,
    val biography: String?,
    val homepage: String?,
    val deathday: String?
)

/**
 * The model to get the celebrity's credit list result.
 *
 * @property id is the celebrity's unique id.
 */
@JsonClass(generateAdapter = true)
data class CelebrityCreditResult(
    val id: Int,
    val cast: List<CombinedMovies>,
    val crew: List<CombinedMovies>
)

/**
 * The model to get the celebrity's images list result.
 *
 * @property id is the celebrity's unique id.
 */
@JsonClass(generateAdapter = true)
data class ActorImageResult(
    val id: Int,
    val profiles: List<Profile>
)

/**
 * The model to get the celebrity images' details result.
 */
@JsonClass(generateAdapter = true)
data class Profile(
    @Json(name = "file_path")
    val filePath: String,
    val height: Int,
    @Json(name = "vote_average")
    val voteAverage: Double,
    @Json(name = "vote_count")
    val voteCount: Int,
    val width: Int
)

/**
 * The model to get the celebrity's social networks' result.
 */
@JsonClass(generateAdapter = true)
data class ExternalLinks(
    val id: Int,
    @Json(name = "imdb_id")
    val imdbId: String?,
    @Json(name = "facebook_id")
    val facebookId: String?,
    @Json(name = "instagram_id")
    val instagramId: String?,
    @Json(name = "twitter_id")
    val twitterId: String?
)

