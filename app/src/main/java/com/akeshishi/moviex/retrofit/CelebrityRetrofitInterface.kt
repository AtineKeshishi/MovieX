package com.akeshishi.moviex.retrofit

import com.akeshishi.moviex.pojo.celebrity.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * The retrofit interface of celebrity services.
 */
interface CelebrityRetrofitInterface {

    @GET("person/popular")
    suspend fun getPopularCelebrityList(@Query("page") page: Int) : PopularCelebritiesResult

    @GET("person/{person_id}")
    suspend fun celebrityDetails(@Path("person_id") id: Int): CelebrityDetails

    @GET("person/{person_id}/movie_credits")
    suspend fun getCelebrityMovieCredits(@Path("person_id") id: Int): CelebrityCreditResult

    @GET("person/{person_id}/tv_credits")
    suspend fun getCelebrityShowCredits(@Path("person_id") id: Int): CelebrityCreditResult

    @GET("person/{person_id}/images")
    suspend fun getCelebrityImages(@Path("person_id") id: Int) : ActorImageResult

    @GET("person/{person_id}/external_ids")
    suspend fun getExternalID(@Path("person_id") id: Int): ExternalLinks
}