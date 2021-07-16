package com.akeshishi.moviex.pojo.shows

import com.akeshishi.moviex.pojo.movies.CreditDetails
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SeasonDetails(
    val id: Int,
    @Json(name = "air_date")
    val airDate: String?,
    val episodes: List<EpisodeListResult>,
    val name: String,
    val overview: String,
    @Json(name = "poster_path")
    val posterPath: String?,
    @Json(name = "season_number")
    val seasonNumber: Int
)

@JsonClass(generateAdapter = true)
data class EpisodeListResult(
    @Json(name = "air_date")
    val airDate: String,
    val crew: List<CreditDetails>,
    @Json(name = "episode_number")
    val episodeNumber: Int,
    @Json(name = "guest_stars")
    val guestStars: List<CreditDetails>,
    val id: Int,
    val name: String,
    val overview: String,
    @Json(name = "season_number")
    val seasonNumber: Int,
    @Json(name = "vote_average")
    val rating: Double,
    @Json(name = "still_path")
    val stillPath: String?
)

@JsonClass(generateAdapter = true)
data class EpisodeDetails(
    val id: Int,
    val name: String,
    val overview: String,
    @Json(name = "air_date")
    val airDate: String,
    @Json(name = "episode_number")
    val episodeNumber: Int,
    @Json(name = "season_number")
    val seasonNumber: Int,
    @Json(name = "still_path")
    val stillPath: String?,
)

@JsonClass(generateAdapter = true)
data class SeasonListResult(
    val id: Int,
    val name: String,
    val overview: String,
    @Json(name = "air_date")
    val airDate: String?,
    @Json(name = "episode_count")
    val episodeCount: Int,
    @Json(name = "poster_path")
    val posterPath: String?,
    @Json(name = "season_number")
    val seasonNumber: Int
)