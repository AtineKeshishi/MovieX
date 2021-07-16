package com.akeshishi.moviex.features.home.adapter

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.akeshishi.moviex.pojo.search.CombinedMovies
import com.akeshishi.moviex.repository.NetworkRepositoryInterface

/**
 * The page source for getting the response and handle pagination of trending movie list in
 * [HomeFragment]
 *
 * @property repository is the repository for getting the response from the server.
 */
class TrendingMoviesPageSource(private val repository: NetworkRepositoryInterface) :
    PagingSource<Int, CombinedMovies>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CombinedMovies> = try {
        val page = params.key ?: 1
        val response = repository.getTrendingMovies(page)
        val previousPage = if (page - 1 > 0) page - 1 else null
        val nextPage = if (response.trendingList.isNullOrEmpty()) null else page + 1
        LoadResult.Page(response.trendingList, previousPage, nextPage)
    } catch (exception: Exception) {
        LoadResult.Error(exception)
    }

    override fun getRefreshKey(state: PagingState<Int, CombinedMovies>): Int? {
        return state.anchorPosition
    }
}