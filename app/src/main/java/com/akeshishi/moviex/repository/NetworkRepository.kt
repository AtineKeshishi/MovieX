package com.akeshishi.moviex.repository

import com.akeshishi.moviex.retrofit.CelebrityRetrofitInterface
import com.akeshishi.moviex.retrofit.MovieRetrofitInterface
import com.akeshishi.moviex.retrofit.ShowRetrofitInterface

/**
 * All network observables are stored here.
 */
class NetworkRepository(
    private val movieRetrofitInterface: MovieRetrofitInterface,
    private val showRetrofitInterface: ShowRetrofitInterface,
    private val celebrityRetrofitInterface: CelebrityRetrofitInterface
) : NetworkRepositoryInterface {

    override suspend fun getTrendingMovies(page: Int) =
        movieRetrofitInterface.getTrendingMovies(page)

    override suspend fun getTrendingShows(page: Int) = showRetrofitInterface.getTrendingShows(page)

    override suspend fun getAiringTodayList(page: Int) =
        showRetrofitInterface.getAiringTodayList(page)

    override suspend fun getGenres(type: String) = movieRetrofitInterface.getGenres(type)

    override suspend fun getPopularCelebrityList(page: Int) =
        celebrityRetrofitInterface.getPopularCelebrityList(page)

    override suspend fun getMovieListByGenre(type: String, page: Int, genre: Int) =
        movieRetrofitInterface.getMovieListByGenre(type, page, genre)

    override suspend fun getPopularMovieList(page: Int) =
        movieRetrofitInterface.getPopularMovieList(page)

    override suspend fun getTopRatedMovieList(page: Int) =
        movieRetrofitInterface.getTopRatedMovieList(page)

    override suspend fun getUpcomingMovieList(page: Int, region: String) =
        movieRetrofitInterface.getUpcomingMovieList(page, region)

    override suspend fun getNowPlayingMovieList(page: Int) =
        movieRetrofitInterface.getNowPlayingMovieList(page)

    override suspend fun getMovieDetails(id: Int) = movieRetrofitInterface.getMovieDetails(id)

    override suspend fun getMovieCredits(id: Int) = movieRetrofitInterface.getMovieCredits(id)

    override suspend fun getMovieVideos(id: Int) = movieRetrofitInterface.getMovieVideos(id)

    override suspend fun getSimilarMovieList(id: Int) =
        movieRetrofitInterface.getSimilarMovieList(id)

    override suspend fun getPopularShowList(page: Int) =
        showRetrofitInterface.getPopularShowList(page)

    override suspend fun getOnAirShowList(page: Int) = showRetrofitInterface.getOnAirShowList(page)

    override suspend fun getShowDetails(id: Int) = showRetrofitInterface.getShowDetails(id)

    override suspend fun getShowCredits(id: Int) = showRetrofitInterface.getShowCredits(id)

    override suspend fun getSeasonDetails(id: Int, seasonNumber: Int) =
        showRetrofitInterface.getSeasonDetails(id, seasonNumber)

    override suspend fun getEpisodeDetails(
        id: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ) = showRetrofitInterface.getEpisodeDetails(id, seasonNumber, episodeNumber)

    override suspend fun celebrityDetails(id: Int) = celebrityRetrofitInterface.celebrityDetails(id)

    override suspend fun getCelebrityMovieCredits(id: Int) =
        celebrityRetrofitInterface.getCelebrityMovieCredits(id)

    override suspend fun getCelebrityShowCredits(id: Int) =
        celebrityRetrofitInterface.getCelebrityShowCredits(id)

    override suspend fun getCelebrityImages(id: Int) =
        celebrityRetrofitInterface.getCelebrityImages(id)

    override suspend fun getExternalID(id: Int) = celebrityRetrofitInterface.getExternalID(id)

    override suspend fun searchMulti(query: String, page: Int) =
        movieRetrofitInterface.searchMulti(query, page)
}