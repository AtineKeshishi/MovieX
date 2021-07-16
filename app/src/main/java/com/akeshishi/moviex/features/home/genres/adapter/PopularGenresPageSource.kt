package com.akeshishi.moviex.features.home.genres.adapter

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.akeshishi.moviex.features.home.genres.PopularGenresFragment
import com.akeshishi.moviex.pojo.search.CombinedMovies
import com.akeshishi.moviex.repository.NetworkRepositoryInterface

/**
 * The page source for getting the response and handle pagination of movie or tv show list in the
 * specified genre in [PopularGenresFragment].
 *
 * @property mediaType is the media type, it is either movie or tv.
 * @property genreId is the genre's unique id.
 * @property repository is the repository for getting the response from the server.
 */
class PopularGenresPageSource(
    private val mediaType: String,
    private val genreId: Int,
    private val repository: NetworkRepositoryInterface
) :
    PagingSource<Int, CombinedMovies>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CombinedMovies> = try {
        val page = params.key ?: 1
        val response = repository.getMovieListByGenre(mediaType, page, genreId)
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