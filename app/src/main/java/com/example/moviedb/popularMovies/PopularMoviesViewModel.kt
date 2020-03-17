package com.example.moviedb.popularMovies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.moviedb.data.Movie
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.moviedb.paging.NetworkState
import io.reactivex.disposables.CompositeDisposable

class PopularMoviesViewModel(private val movieRepository : MoviePagedListRepository) : ViewModel(){

    private val compositeDisposable = CompositeDisposable()
//    var repo : LiveData<PagedList<Movie>>

    val moviePagedList: LiveData<PagedList<Movie>> by lazy {
                movieRepository.fetchLiveMoviePagedList()
            }

    val  networkState : LiveData<NetworkState> by lazy {
        movieRepository.getNetworkState()
    }

    private val searchQuery = MutableLiveData<String>()
//    private val repoResult: LiveData<SearchResult> = Transformations.map(searchQuery) {
//    movieRepository.search(it)
//}
//
//    init {
//        repo = Transformations.switchMap(repoResult) { it -> it.data }
//    }

    fun listIsEmpty(): Boolean {
        return moviePagedList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    //Search based on a query string
    fun searchMovie(queryString: String) {
            searchQuery.postValue(queryString)
    }

}