package com.example.moviedb.favoriteMovies

import androidx.lifecycle.LiveData
import com.example.moviedb.database.MovieDao
import com.example.moviedb.database.MovieEntity

class MovieDetailRepository(private val movieDao: MovieDao) {

    // Observed LiveData will notify the observer when the data has changed.
    val allMovies : LiveData<List<MovieEntity>> = movieDao.loadAll()

    fun insert(movieEntity: MovieEntity){
        movieDao.insert(movieEntity)
    }

    fun deleteItem (id : Int){
        movieDao.delete(id)
    }

}