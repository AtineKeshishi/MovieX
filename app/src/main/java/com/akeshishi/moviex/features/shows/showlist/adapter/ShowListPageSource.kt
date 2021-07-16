package com.akeshishi.moviex.features.shows.showlist.adapter

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.akeshishi.moviex.pojo.menu.MoviesSection
import com.akeshishi.moviex.pojo.menu.MoviesSection.NOW_PLAYING
import com.akeshishi.moviex.pojo.menu.MoviesSection.POPULAR
import com.akeshishi.moviex.pojo.search.CombinedMovies
import com.akeshishi.moviex.repository.NetworkRepositoryInterface

/**
 * The page source for getting the response and handle pagination of tv show list in
 * [ShowListFragment].
 *
 * @property selectedSection is the different movie list collections.
 * @property repository is the repository for getting the response from the server.
 */

class ShowListPageSource(
    private val selectedSection: MoviesSection,
    private val repository: NetworkRepositoryInterface
) :
    PagingSource<Int, CombinedMovies>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CombinedMovies> = try {
        val page = params.key ?: 1
        val response = when(selectedSection) {
            POPULAR -> repository.getPopularShowList(page)
            NOW_PLAYING -> repository.getOnAirShowList(page)
            else -> TODO()
        }
        val previousPage = if (page - 1 > 0) page - 1 else null
        val nextPage = if (response.showList.isNullOrEmpty()) null else page + 1
        LoadResult.Page(response.showList, previousPage, nextPage)
    } catch (e: Exception) {
        LoadResult.Error(e)
    }

    override fun getRefreshKey(state: PagingState<Int, CombinedMovies>): Int? {
        return state.anchorPosition
    }
}