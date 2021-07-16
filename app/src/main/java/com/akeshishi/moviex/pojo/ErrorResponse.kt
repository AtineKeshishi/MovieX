package com.akeshishi.moviex.pojo


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * The model to get the error result.
 */
@JsonClass(generateAdapter = true)
data class ErrorResponse(
    @Json(name = "status_code")
    val statusCode: Int,
    @Json(name = "status_message")
    val statusMessage: String,
    val success: Boolean
)