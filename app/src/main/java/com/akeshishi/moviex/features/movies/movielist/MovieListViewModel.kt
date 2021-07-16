package com.akeshishi.moviex.features.movies.movielist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.akeshishi.moviex.features.movies.movielist.adapter.MovieListPageSource
import com.akeshishi.moviex.pojo.menu.MoviesSection
import com.akeshishi.moviex.pojo.search.CombinedMovies
import com.akeshishi.moviex.repository.NetworkRepositoryInterface
import com.akeshishi.moviex.utils.LIST_SIZE
import kotlinx.coroutines.flow.Flow

/**
 * ViewModel class for [MovieListFragment].
 * In this class, the services of the [MovieListFragment] are being called.
 * @see com.akeshishi.moviex.retrofit.MovieRetrofitInterface
 *
 * @property repository is the repository to call API service.
 */
class MovieListViewModel(private val repository: NetworkRepositoryInterface) : ViewModel() {

    var movieDataFlow: Flow<PagingData<CombinedMovies>>? = null

    fun getMovieList(selectedSection: MoviesSection) {
        if (movieDataFlow == null) {
            movieDataFlow = Pager(PagingConfig(pageSize = LIST_SIZE)) {
                MovieListPageSource(selectedSection, repository)
            }.flow.cachedIn(viewModelScope)
        }
    }
}