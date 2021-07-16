package com.akeshishi.moviex.features.movies.movielist.adapter

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.akeshishi.moviex.pojo.menu.MoviesSection
import com.akeshishi.moviex.pojo.menu.MoviesSection.*
import com.akeshishi.moviex.pojo.search.CombinedMovies
import com.akeshishi.moviex.repository.NetworkRepositoryInterface
import com.akeshishi.moviex.utils.REGION

/**
 * The page source for getting the response and handle pagination of movie list in
 * [MovieListFragment].
 *
 * @property selectedSection is the different movie list collections.
 * @property repository is the repository for getting the response from the server.
 */
class MovieListPageSource(
    private val selectedSection: MoviesSection,
    private val repository: NetworkRepositoryInterface
) : PagingSource<Int, CombinedMovies>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CombinedMovies> = try {
        val page = params.key ?: 1
        val response = when (selectedSection) {
            POPULAR -> repository.getPopularMovieList(page)
            NOW_PLAYING -> repository.getNowPlayingMovieList(page)
            TOP_RATED -> repository.getTopRatedMovieList(page)
            UPCOMING -> repository.getUpcomingMovieList(page, REGION)
        }
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