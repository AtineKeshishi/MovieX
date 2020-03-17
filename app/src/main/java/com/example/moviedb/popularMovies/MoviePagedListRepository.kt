package com.example.moviedb.popularMovies

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.moviedb.api.ApiInterface
import com.example.moviedb.api.POST_PER_PAGE
import com.example.moviedb.data.Movie
import com.example.moviedb.paging.MovieDataSource
import com.example.moviedb.paging.MovieDataSourceFactory
import com.example.moviedb.paging.NetworkState
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.Executor

class MoviePagedListRepository  (private val apiService : ApiInterface, private val networkExecutor: Executor) {
//
    lateinit var moviePagedList: LiveData<PagedList<Movie>>
    private var moviesDataSourceFactory: MovieDataSourceFactory
    private val compositeDisposable = CompositeDisposable()
    private var config : PagedList.Config
    val query: String = ""

init {
    moviesDataSourceFactory = MovieDataSourceFactory(apiService, compositeDisposable,query)
    config = PagedList.Config.Builder().setEnablePlaceholders(false).setPageSize(POST_PER_PAGE).build()
             config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()
}

    fun fetchLiveMoviePagedList () : LiveData<PagedList<Movie>> {
        // Get the paged list
        moviePagedList = LivePagedListBuilder(moviesDataSourceFactory, config).setFetchExecutor(networkExecutor).build()
        return moviePagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap(moviesDataSourceFactory.moviesLiveDataSource, MovieDataSource::networkState)
    }


//    fun search(query: String) : SearchResult {
//        val data = LivePagedListBuilder(moviesDataSourceFactory, config).setFetchExecutor(networkExecutor).build()
//        return SearchResult(data)
//    }
}

////SearchResult from a search, which contains LiveData<List<Movie>> holding query data.
//data class SearchResult(val data: LiveData<PagedList<Movie>>)