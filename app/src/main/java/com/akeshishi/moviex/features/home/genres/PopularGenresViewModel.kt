package com.akeshishi.moviex.features.home.genres

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.akeshishi.moviex.features.home.genres.adapter.PopularGenresPageSource
import com.akeshishi.moviex.pojo.search.CombinedMovies
import com.akeshishi.moviex.repository.NetworkRepositoryInterface
import com.akeshishi.moviex.utils.LIST_SIZE
import kotlinx.coroutines.flow.Flow

/**
 * ViewModel class of [PopularGenresFragment].
 *
 * @property genreId is the genre's unique id.
 * @property repository is the repository to call API service.
 */
class PopularGenresViewModel(
    private val genreId: Int,
    private val repository: NetworkRepositoryInterface
) : ViewModel() {

    var celebrityDataFlow: Flow<PagingData<CombinedMovies>>? = null

    fun getMovieList(mediaType: String) {
        if (celebrityDataFlow == null) {
            celebrityDataFlow = Pager(PagingConfig(pageSize = LIST_SIZE)) {
                PopularGenresPageSource(mediaType, genreId, repository)
            }.flow.cachedIn(viewModelScope)
        }
    }
}