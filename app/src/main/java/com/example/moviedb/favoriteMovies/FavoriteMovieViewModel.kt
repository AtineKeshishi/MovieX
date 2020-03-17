package com.example.moviedb.favoriteMovies

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviedb.database.MovieDatabase
import com.example.moviedb.database.MovieEntity
import kotlinx.coroutines.launch

class FavoriteMovieViewModel(application: Application) : AndroidViewModel(application) {

    // The ViewModel maintains a reference to the repository to get data.
    private val repository : MovieDetailRepository

    // LiveData gives updated words when they change.
    val getAllMovies : LiveData<List<MovieEntity>>

    init {
        // Gets reference to MovieDao from MovieDatabase to construct the correct MovieDetailRepository.
        val movieDao = MovieDatabase.getDatabase(application).movieDao()
        repository = MovieDetailRepository(movieDao)
        getAllMovies = repository.allMovies
    }

}
