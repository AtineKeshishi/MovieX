package com.example.moviedb.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movie_table")
//data class MovieEntity(
//    @PrimaryKey
//    @ColumnInfo(name = "id")
//    val id: Int,
//    @ColumnInfo(name = "posterPath")
//    val posterPath: String,
//    @ColumnInfo(name = "title")
//    val title: String,
//    @ColumnInfo(name = "releaseDate")
//    val releaseDate: String,
//    @ColumnInfo(name = "rating")
//    val rating: String,
//    @ColumnInfo(name = "runtime")
//    val runtime: String,
//    @ColumnInfo(name = "budget")
//    val budget: String,
//    @ColumnInfo(name = "overview")
//    val overview: String
//)

 class MovieEntity{
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int = 0
    @ColumnInfo(name = "posterPath")
    var posterPath: String = ""
    @ColumnInfo(name = "title")
    var title: String = ""
    @ColumnInfo(name = "releaseDate")
    var releaseDate: String = ""
    @ColumnInfo(name = "rating")
    var rating: String = ""
    @ColumnInfo(name = "runtime")
    var runtime: String = ""
    @ColumnInfo(name = "budget")
    var budget: String = ""
    @ColumnInfo(name = "overview")
    var overview: String = ""
}