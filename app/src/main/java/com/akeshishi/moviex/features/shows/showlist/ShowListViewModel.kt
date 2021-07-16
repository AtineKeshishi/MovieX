package com.akeshishi.moviex.features.shows.showlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.akeshishi.moviex.features.shows.showlist.adapter.ShowListPageSource
import com.akeshishi.moviex.pojo.menu.MoviesSection
import com.akeshishi.moviex.pojo.search.CombinedMovies
import com.akeshishi.moviex.repository.NetworkRepositoryInterface
import com.akeshishi.moviex.utils.LIST_SIZE
import kotlinx.coroutines.flow.Flow

/**
 * ViewModel class for [ShowListFragment].
 * In this class, the services of the [ShowListFragment] are being called.
 * @see ShowRetrofitInterface
 *
 * @property repository is the repository to call API service.
 */
class ShowListViewModel(private val repository: NetworkRepositoryInterface) : ViewModel() {

    var showDataFlow: Flow<PagingData<CombinedMovies>>? = null

    fun getShowList(selectedSection: MoviesSection){
        if (showDataFlow == null) {
            showDataFlow = Pager(PagingConfig(pageSize = LIST_SIZE)) {
                ShowListPageSource(selectedSection, repository)
            }.flow.cachedIn(viewModelScope)
        }
    }
}