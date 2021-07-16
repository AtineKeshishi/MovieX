package com.akeshishi.moviex.pojo.shows

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * The model to get the tv show's creator's details' result.
 */
@JsonClass(generateAdapter = true)
data class Creators(
    val id: Int,
    @Json(name = "credit_id")
    val creditId: String,
    val name: String,
    @Json(name = "profile_path")
    val profilePath: String?
)

/**
 * The model to get the tv show's Network's details' result.
 */
@JsonClass(generateAdapter = true)
data class Network(
    val id: Int,
    val name: String,
    @Json(name = "logo_path")
    val logoPath: String?,
    @Json(name = "origin_country")
    val originCountry: String
)

/**
 * The model to get the movie or tv show's production company's details' result.
 */
@JsonClass(generateAdapter = true)
data class ProductionCompany(
    val id: Int,
    val name: String,
    @Json(name = "logo_path")
    val logoPath: String?,
    @Json(name = "origin_country")
    val originCountry: String
)

/**
 * The model to get the movie or tv show's production country's details' result.
 */
@JsonClass(generateAdapter = true)
data class ProductionCountry(
    @Json(name = "iso_3166_1")
    val iso3: String,
    val name: String
)

/**
 * The model to get the movie or tv show's spoken languages' result.
 */
@JsonClass(generateAdapter = true)
data class SpokenLanguage(
    @Json(name = "english_name")
    val englishName: String,
    @Json(name = "iso_639_1")
    val iso6391: String,
    val name: String
)