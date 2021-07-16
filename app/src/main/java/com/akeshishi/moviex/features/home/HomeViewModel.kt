package com.akeshishi.moviex.features.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.akeshishi.moviex.features.home.adapter.AiringTodayListPageSource
import com.akeshishi.moviex.features.home.adapter.CelebrityListPageSource
import com.akeshishi.moviex.features.home.adapter.TrendingMoviesPageSource
import com.akeshishi.moviex.features.home.adapter.TrendingShowsPageSource
import com.akeshishi.moviex.pojo.celebrity.PopularCelebrities
import com.akeshishi.moviex.pojo.movies.GenresResult
import com.akeshishi.moviex.pojo.search.CombinedMovies
import com.akeshishi.moviex.repository.NetworkRepositoryInterface
import com.akeshishi.moviex.utils.GeneralResponse
import com.akeshishi.moviex.utils.LIST_SIZE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * ViewModel class of [HomeFragment].
 * In this class, the services of the [HomeFragment] are being called.
 * @see com.akeshishi.moviex.retrofit.MovieRetrofitInterface
 * @see com.akeshishi.moviex.retrofit.ShowRetrofitInterface
 * @see com.akeshishi.moviex.retrofit.CelebrityRetrofitInterface
 *
 * @property repository is the repository to call API service.
 */
class HomeViewModel(private val repository: NetworkRepositoryInterface) : ViewModel() {

    var trendingMovieDataFlow: Flow<PagingData<CombinedMovies>>? = null
    var trendingShowDataFlow: Flow<PagingData<CombinedMovies>>? = null
    var celebrityDataFlow: Flow<PagingData<PopularCelebrities>>? = null
    var airingDataFlow: Flow<PagingData<CombinedMovies>>? = null



    private val movieGenreLiveData = MutableLiveData<GeneralResponse<GenresResult>>()
    fun getMovieGenreLiveData(): MutableLiveData<GeneralResponse<GenresResult>> = movieGenreLiveData


    fun getTrendingMovies() {
        if (trendingMovieDataFlow == null) {
            trendingMovieDataFlow = Pager(PagingConfig(pageSize = LIST_SIZE)) {
                TrendingMoviesPageSource(repository)
            }.flow.cachedIn(viewModelScope)
        }
    }

    fun getTrendingShows() {
        if (trendingShowDataFlow == null) {
            trendingShowDataFlow = Pager(PagingConfig(pageSize = LIST_SIZE)) {
                TrendingShowsPageSource(repository)
            }.flow.cachedIn(viewModelScope)
        }
    }

    fun getAiringTodayList() {
        if (airingDataFlow == null) {
            airingDataFlow = Pager(PagingConfig(pageSize = LIST_SIZE)) {
                AiringTodayListPageSource(repository)
            }.flow.cachedIn(viewModelScope)
        }
    }

    fun getCelebrityList() {
        if (celebrityDataFlow == null) {
            celebrityDataFlow = Pager(PagingConfig(pageSize = LIST_SIZE)) {
                CelebrityListPageSource(repository)
            }.flow.cachedIn(viewModelScope)
        }
    }

    fun getMovieGenres(mediaType: String) {
        movieGenreLiveData.value = GeneralResponse.Loading()
        viewModelScope.launch {
            try {
                val response = repository.getGenres(mediaType)
                movieGenreLiveData.value = GeneralResponse.Success(response)
            } catch (exception: Exception){
                movieGenreLiveData.value = GeneralResponse.Error(exception)
            }
        }
    }
}