package com.akeshishi.moviex.features.celebrity.adapter

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.akeshishi.moviex.pojo.search.CombinedMovies
import com.akeshishi.moviex.repository.NetworkRepositoryInterface
import com.akeshishi.moviex.utils.MOVIE_CREDITS

/**
 * The page source for getting the response and handle pagination of celebrity's credit list in
 * [CelebrityCreditFragment].
 *
 * @property mediaType is the media type, it is either movie or tv.
 * @property personId is the celebrity's unique id.
 * @property repository is the repository for getting the response from the server.
 */
class CelebrityCreditPageSource(
    private val mediaType: String,
    private val personId: Int,
    private val repository: NetworkRepositoryInterface
) :
    PagingSource<Int, CombinedMovies>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CombinedMovies> = try {
        val response = when (mediaType) {
            MOVIE_CREDITS -> repository.getCelebrityMovieCredits(personId)
            else -> repository.getCelebrityShowCredits(personId)
        }
        LoadResult.Page((response.cast).plus(response.crew), null, null)
    } catch (exception: Exception) {
        LoadResult.Error(exception)
    }

    override fun getRefreshKey(state: PagingState<Int, CombinedMovies>): Int? {
        return state.anchorPosition
    }
}