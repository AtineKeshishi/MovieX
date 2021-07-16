package com.akeshishi.moviex.features.movies.similar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.akeshishi.moviex.features.movies.similar.adapter.SimilarMovieListPageSource
import com.akeshishi.moviex.pojo.search.CombinedMovies
import com.akeshishi.moviex.repository.NetworkRepositoryInterface
import com.akeshishi.moviex.utils.LIST_SIZE
import kotlinx.coroutines.flow.Flow

/**
 * ViewModel class of [SimilarMovieFragment].
 * In this class, the services of the [SimilarMovieFragment] are being called.
 * @see MovieRetrofitInterface
 *
 * @property movieId is the movie's unique id.
 * @property repository is the repository to call API service.
 */
class SimilarMovieViewModel(
    private val movieId: Int,
    private val repository: NetworkRepositoryInterface
) : ViewModel() {

    var similarDataFlow: Flow<PagingData<CombinedMovies>>? = null

    fun getSimilarMovieList() {
        if (similarDataFlow == null) {
            similarDataFlow = Pager(PagingConfig(LIST_SIZE)) {
                SimilarMovieListPageSource(movieId, repository)
            }.flow.cachedIn(viewModelScope)
        }
    }
}