package com.akeshishi.moviex.retrofit

import com.akeshishi.moviex.pojo.movies.CreditResult
import com.akeshishi.moviex.pojo.movies.TrendingResult
import com.akeshishi.moviex.pojo.shows.EpisodeListResult
import com.akeshishi.moviex.pojo.shows.SeasonDetails
import com.akeshishi.moviex.pojo.shows.ShowDetails
import com.akeshishi.moviex.pojo.shows.ShowListResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * The retrofit interface of tv show services.
 */
interface ShowRetrofitInterface {

    @GET("trending/tv/day")
    suspend fun getTrendingShows(@Query("page") page: Int): TrendingResult

    @GET("tv/airing_today")
    suspend fun getAiringTodayList(@Query("page") page: Int): TrendingResult

    @GET("tv/popular")
    suspend fun getPopularShowList(@Query("page") page: Int): ShowListResult

    @GET("tv/on_the_air")
    suspend fun getOnAirShowList(@Query("page") page: Int): ShowListResult

    @GET("tv/{tv_id}")
    suspend fun getShowDetails(@Path("tv_id") id: Int): ShowDetails

    @GET("tv/{tv_id}/credits")
    suspend fun getShowCredits(@Path("tv_id") id: Int): CreditResult

    @GET("tv/{tv_id}/season/{season_number}")
    suspend fun getSeasonDetails(
        @Path("tv_id") id: Int,
        @Path("season_number") seasonNumber: Int
    ): SeasonDetails

    @GET("tv/{tv_id}/season/{season_number}/episode/{episode_number}")
    suspend fun getEpisodeDetails(
        @Path("tv_id") id: Int,
        @Path("season_number") seasonNumber: Int,
        @Path("episode_number") episodeNumber: Int
    ): EpisodeListResult
}