package com.akeshishi.moviex.features.movies.similar.adapter

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.akeshishi.moviex.pojo.search.CombinedMovies
import com.akeshishi.moviex.repository.NetworkRepositoryInterface

/**
 * The page source for getting the response and handle pagination of similar movie list in
 * [SimilarMovieFragment].
 *
 * @property movieId is the movie's unique id.
 * @property repository is the repository for getting the response from the server.
 */
class SimilarMovieListPageSource(
    private val movieId: Int,
    private val repository: NetworkRepositoryInterface,
) : PagingSource<Int, CombinedMovies>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CombinedMovies> = try {
        val page = params.key ?: 1
        val response = repository.getSimilarMovieList(movieId)
        val previousPage = if (page - 1 > 0) page - 1 else null
        val nextPage = if (response.movieList.isNullOrEmpty()) null else page + 1
        LoadResult.Page(response.movieList, previousPage, nextPage)
    } catch (exception: Exception) {
        LoadResult.Error(exception)
    }

    override fun getRefreshKey(state: PagingState<Int, CombinedMovies>): Int? {
        return state.anchorPosition
    }
}