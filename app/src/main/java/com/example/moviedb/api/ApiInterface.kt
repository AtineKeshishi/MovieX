package com.example.moviedb.api

import android.util.Log
import com.example.moviedb.data.Movie
import com.example.moviedb.data.MovieDetails
import com.example.moviedb.data.MovieResponse
import com.example.moviedb.data.SearchResponse
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


// https://api.themoviedb.org/3/movie/popular?api_key=9c1b50192814698baf50b09d496c86fc&page=1
// https://api.themoviedb.org/3/movie/299534?api_key=9c1b50192814698baf50b09d496c86fc
//https://api.themoviedb.org/3/search/movie?api_key=9c1b50192814698baf50b09d496c86fc&query=Jack%2BReacher
// https://api.themoviedb.org/3/

interface ApiInterface {

    @GET("movie/popular")
    fun getPopularMovie(@Query("page") page: Int): Single<MovieResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Int): Single<MovieDetails>

    @GET("search/movie")
    fun searchMovie (@Query("query") query: String): Single<SearchResponse>
}

//fun searchMovie(
//    service: ApiInterface,
//    query: String,
//    onSuccess: (repos: List<Movie>) -> Unit,
//    onError: (error: String) -> Unit
//)
//{
//    Log.d("Movie", "query: $query")
//    Log.d("Movie", "ApiInterface searchMovie called")
//
//    service.searchMovie(query).enqueue(object : Callback<MovieResponse>{
//        override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
//            Log.d("Movie", "got a response $response")
//            if (response.isSuccessful){
//                val repo = response.body()?.movieList ?: emptyList()
//                onSuccess(repo)
//            }
//        }
//
//        override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
//            Log.d("Movie", "fail to get data")
//            onError(t.message ?: "Unknown error")
//        }
//    })
//}