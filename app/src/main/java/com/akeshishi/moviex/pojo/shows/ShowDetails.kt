package com.akeshishi.moviex.pojo.shows

import com.akeshishi.moviex.pojo.movies.Genre
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * The model to get the tv show details' result.
 */
@JsonClass(generateAdapter = true)
data class ShowDetails(
    val id: Int,
    val name: String,
    @Json(name = "backdrop_path")
    val backdropPath: String?,
    @Json(name = "created_by")
    val createdBy: List<Creators>,
    @Json(name = "episode_run_time")
    val episodeRunTime: List<Int>,
    @Json(name = "first_air_date")
    val firstAirDate: String,
    val genres: List<Genre>,
    val homepage: String?,
    @Json(name = "in_production")
    val inProduction: Boolean,
    val languages: List<String>,
    @Json(name = "last_air_date")
    val lastAirDate: String?,
    @Json(name = "last_episode_to_air")
    val lastEpisodeToAir: EpisodeDetails?,
    val networks: List<Network>,
    @Json(name = "next_episode_to_air")
    val nextEpisodeToAir: EpisodeDetails?,
    @Json(name = "number_of_episodes")
    val numberOfEpisodes: Int,
    @Json(name = "number_of_seasons")
    val numberOfSeasons: Int,
    @Json(name = "origin_country")
    val originCountry: List<String>,
    @Json(name = "original_language")
    val originalLanguage: String,
    @Json(name = "original_name")
    val originalName: String,
    val overview: String?,
    val popularity: Double,
    @Json(name = "poster_path")
    val posterPath: String?,
    @Json(name = "production_companies")
    val productionCompanies: List<ProductionCompany>,
    @Json(name = "production_countries")
    val productionCountries: List<ProductionCountry>,
    @Json(name = "spoken_languages")
    val spokenLanguages: List<SpokenLanguage>,
    val seasons: List<SeasonListResult>,
    val status: String,
    @Json(name = "vote_average")
    val voteAverage: Double,
)