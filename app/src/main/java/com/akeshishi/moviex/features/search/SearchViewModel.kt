package com.akeshishi.moviex.features.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.akeshishi.moviex.features.search.adapter.SearchResultPageSource
import com.akeshishi.moviex.pojo.search.CombinedMovies
import com.akeshishi.moviex.repository.NetworkRepositoryInterface
import com.akeshishi.moviex.utils.LIST_SIZE
import kotlinx.coroutines.flow.Flow

/**
 * ViewModel class of [SearchFragment].
 * In this class, the services of the [SearchFragment] are being called.
 * @see MovieRetrofitInterface
 *
 * @property repository is the repository to call API service.
 */
class SearchViewModel(private val repository: NetworkRepositoryInterface) : ViewModel() {

    private var searchTerm = ""
    private var lastSearchedTerm = ""

    var searchDataFlow: Flow<PagingData<CombinedMovies>>? = null

    fun search(query: String) {
        if (query == lastSearchedTerm) return
        searchDataFlow = Pager(PagingConfig(pageSize = LIST_SIZE)) {
            SearchResultPageSource(query, repository)
        }.flow.cachedIn(viewModelScope)
        lastSearchedTerm = query
    }


    /**
     * This function saves the searched phrase.
     *
     * @param term is the searched phrase by the user.
     */
    fun saveSearchTerm(term: String) {
        searchTerm = term
    }

    /**
     * Provide the phrase searched by the user in every search operation.
     */
    fun getSearchTerm() = searchTerm
}