package com.akeshishi.moviex.pojo.movies

import com.squareup.moshi.JsonClass

/**
 * The model to get the movie trailers' list result.
 *
 * @property id is the movie's uniqu id.
 */
@JsonClass(generateAdapter = true)
data class VideoResult(
    val id: Int,
    val results: List<Videos>
)

/**
 * The model to get the movie trailers details' result.
 */
@JsonClass(generateAdapter = true)
data class Videos(
    val id: String,
    val key: String,
    val name: String,
    val type: String
)