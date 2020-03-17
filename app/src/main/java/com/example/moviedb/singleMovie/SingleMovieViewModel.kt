package com.example.moviedb.singleMovie

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.moviedb.paging.MovieDetailsDataSource
import com.example.moviedb.paging.NetworkState
import com.example.moviedb.api.ApiClient
import com.example.moviedb.data.MovieDetails
import com.example.moviedb.database.MovieDatabase
import com.example.moviedb.database.MovieEntity
import com.example.moviedb.favoriteMovies.MovieDetailRepository
import io.reactivex.disposables.CompositeDisposable


class SingleMovieViewModel(movieId: Int, application: Application) : AndroidViewModel(application) {

    private val compositeDisposable = CompositeDisposable()
    private val apiService = ApiClient.getClient()
    private lateinit var movieDetailsDataSource: MovieDetailsDataSource
    private val repository : MovieDetailRepository

    val movieDetails : LiveData<MovieDetails> by lazy {
        fetchSingleMovieDetails(compositeDisposable,movieId)
    }

    init {
        val movieDao = MovieDatabase.getDatabase(application).movieDao()
        repository = MovieDetailRepository(movieDao)
    }

    private fun fetchSingleMovieDetails (compositeDisposable: CompositeDisposable, movieId: Int) : LiveData<MovieDetails> {

        movieDetailsDataSource = MovieDetailsDataSource(apiService,compositeDisposable)
        movieDetailsDataSource.fetchMovieDetails(movieId)

        return movieDetailsDataSource.movieResponse
    }

    fun getMovieDetailsNetworkState(): LiveData<NetworkState> {
        return movieDetailsDataSource.networkState
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun insert(movie: MovieEntity) {
        repository.insert(movie)
    }

}
