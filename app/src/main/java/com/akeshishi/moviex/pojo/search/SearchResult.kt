package com.akeshishi.moviex.pojo.search

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * The model to get the search list result.
 */
@JsonClass(generateAdapter = true)
data class SearchResult(
    val page: Int,
    @Json(name = "results")
    val multiList: List<CombinedMovies>,
    @Json(name = "total_pages")
    val totalPages: Int,
    @Json(name = "total_results")
    val totalResults: Int
)

/**
 * The model to get the movie or tv show details' result.
 *
 *  @property knownFor is a list of movies or tv shows, which the celebrity is known for.
 * @property name is the tv show's title.
 * @property title is the movie's title.
 */
@JsonClass(generateAdapter = true)
data class CombinedMovies(
    val id: Int,
    @Json(name = "vote_average")
    val rating: String?,
    val overview: String?,
    @Json(name = "poster_path")
    val posterPath: String?,
    @Json(name = "genre_ids")
    var genre: List<Int>?,
    @Json(name = "backdrop_path")
    val backdropPath: String?,
    @Json(name = "media_type")
    val mediaType: String?,

    val title: String?,
    @Json(name = "release_date")
    val releaseDate: String?,
    val character: String?,

    val name: String?,
    @Json(name = "first_air_date")
    val firstAirDate: String?,

    val job: String?,
    val department: String?,
    @Json(name = "known_for")
    val knownFor: List<KnownFor>?,
    val popularity: Double?,
    @Json(name = "profile_path")
    val profilePath: String?,
)

/**
 * The model to get the celebrity's famous media details' result.
 *
 * @property id is the celebrity's unique id.
 * @property mediaType is the media type, it is either movie or tv.
 * @property name is the tv show's title.
 * @property title is the movie's title.
 */
@JsonClass(generateAdapter = true)
data class KnownFor(
    val id: Int?,
    @Json(name = "media_type")
    val mediaType: String?,
    val name: String?,
    val title: String?,
)