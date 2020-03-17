package com.example.moviedb.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.R
import com.example.moviedb.api.POSTER_BASE_URL
import com.example.moviedb.database.MovieDatabase
import com.example.moviedb.database.MovieEntity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.favorite_movie_list.view.*
import kotlinx.android.synthetic.main.popular_movie_list.view.movieTitle

class FavoriteAdapter(context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val database = MovieDatabase.getDatabase(context)

    private var movieItem = emptyList<MovieEntity>() // Cached copy of words

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View
            view = layoutInflater.inflate(R.layout.favorite_movie_list, parent, false)
            return OfflineViewHolder(view)
    }

    override fun getItemCount(): Int = movieItem.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = movieItem[position]
        (holder as OfflineViewHolder).bind(item)
    }
    class OfflineViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
       fun bind(movieEnt: MovieEntity){
            itemView.movieTitle.text = movieEnt.title
           itemView.movieRating.text = movieEnt.rating
           itemView.releaseDate.text = movieEnt.releaseDate

           val moviePosterURL = POSTER_BASE_URL + movieEnt.posterPath
           Picasso.get().load(moviePosterURL).into(itemView.poster)
        }
    }

    fun updateList(movie: List<MovieEntity>) {
        this.movieItem = movie
        notifyDataSetChanged()
    }
}
