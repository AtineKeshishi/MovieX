package com.example.moviedb.paging

import androidx.lifecycle.MutableLiveData
import com.example.moviedb.api.ApiInterface
import io.reactivex.disposables.CompositeDisposable
import androidx.paging.DataSource
import com.example.moviedb.data.Movie

class MovieDataSourceFactory(
    private val apiService: ApiInterface,
    private val compositeDisposable: CompositeDisposable,
    private val query: String
)
    :DataSource.Factory<Int, Movie>(){

    val moviesLiveDataSource =  MutableLiveData<MovieDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val movieDataSource = MovieDataSource(apiService, compositeDisposable, query)
        moviesLiveDataSource.postValue(movieDataSource)

        return movieDataSource
    }
}