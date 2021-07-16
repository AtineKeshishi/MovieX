package com.akeshishi.moviex.features.home.adapter

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.akeshishi.moviex.pojo.celebrity.PopularCelebrities
import com.akeshishi.moviex.repository.NetworkRepositoryInterface

/**
 * The page source for getting the response and handle pagination of popular celebrities list in
 * [HomeFragment]
 *
 * @property repository is the repository for getting the response from the server.
 */
class CelebrityListPageSource(private val repository: NetworkRepositoryInterface) :
    PagingSource<Int, PopularCelebrities>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PopularCelebrities> = try {
        val page = params.key ?: 1
        val response = repository.getPopularCelebrityList(page)
        val previousPage = if (page - 1 > 0) page - 1 else null
        val nextPage = if (response.results.isNullOrEmpty()) null else page + 1
        LoadResult.Page(response.results, previousPage, nextPage)
    } catch (exception: Exception) {
        LoadResult.Error(exception)
    }

    override fun getRefreshKey(state: PagingState<Int, PopularCelebrities>): Int? {
        return state.anchorPosition
    }
}