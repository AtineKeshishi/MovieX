package com.akeshishi.moviex.retrofit

import com.akeshishi.moviex.pojo.movies.*
import com.akeshishi.moviex.pojo.search.SearchResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * The retrofit interface of movie services.
 */
interface MovieRetrofitInterface {

    @GET("trending/movie/day")
    suspend fun getTrendingMovies(@Query("page") page: Int): TrendingResult

    @GET("movie/popular")
    suspend fun getPopularMovieList(@Query("page") page: Int): MovieListResult

    @GET("movie/top_rated")
    suspend fun getTopRatedMovieList(@Query("page") page: Int): MovieListResult

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovieList(@Query("page") page: Int): MovieListResult

    @GET("movie/upcoming")
    suspend fun getUpcomingMovieList(
        @Query("page") page: Int,
        @Query("region") region: String
    ): MovieListResult

    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovieList(@Path("movie_id") id: Int): MovieListResult

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") id: Int): MovieDetails

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(@Path("movie_id") id: Int): CreditResult

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(@Path("movie_id") id: Int): VideoResult

    @GET("genre/{media_type}/list")
    suspend fun getGenres(@Path("media_type") type: String) : GenresResult

    @GET("discover/{media_type}")
    suspend fun getMovieListByGenre(
        @Path("media_type") type: String,
        @Query("page") page: Int,
        @Query("with_genres") genre: Int
    ): MovieListResult

    @GET("search/multi")
    suspend fun searchMulti(
        @Query("query") query: String,
        @Query("page") page: Int
    ): SearchResult
}