package com.example.moviedb.database

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moviedb.data.Movie


@Dao
interface MovieDao {

    @Query("SELECT * from movie_table")
    fun loadAll(): LiveData<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(movieEntity: MovieEntity)

    @Query("DELETE from movie_table where id = :id")
    fun delete(id : Int)

}