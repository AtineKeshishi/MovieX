package com.example.moviedb.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.paging.NetworkState
import com.example.moviedb.R
import com.example.moviedb.api.POSTER_BASE_URL
import com.example.moviedb.data.Movie
import com.example.moviedb.database.MovieEntity
import com.example.moviedb.popularMovies.PopularMoviesDirections
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.network_state_item.view.*
import kotlinx.android.synthetic.main.popular_movie_list.view.*
import kotlinx.android.synthetic.main.popular_movie_list.view.moviePoster
import kotlinx.android.synthetic.main.popular_movie_list.view.movieTitle



class PopularMoviesAdapter: PagedListAdapter<Movie, RecyclerView.ViewHolder>(DiffCallBack()){

    val MOVIE_VIEW = 1
    val NETWORK_VIEW = 2

    private var networkState: NetworkState? = null
    var movie : MovieEntity? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View

        if (viewType == MOVIE_VIEW){
            view = layoutInflater.inflate(R.layout.popular_movie_list, parent, false)
            return MovieViewHolder(view)
        } else {
            view = layoutInflater.inflate(R.layout.network_state_item, parent, false)
            return NetworkStateViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 0 else 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if(item != null){
            if (getItemViewType(position) == MOVIE_VIEW) {
                (holder as MovieViewHolder).bind(item)
            } else {
                (holder as NetworkStateViewHolder).bind(networkState)
            }
        }

    }

    class MovieViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        fun bind(movie: Movie?) {
            itemView.movieTitle.text = movie?.title
            itemView.movieReleaseDate.text =  movie?.releaseDate

            val moviePosterURL = POSTER_BASE_URL + movie?.posterPath
            Picasso.get().load(moviePosterURL).into(itemView.moviePoster)

            itemView.setOnClickListener{
                val direction = PopularMoviesDirections.actionPopularMoviesToMovieDetail(movie!!.id)
                Navigation.findNavController(itemView).navigate(direction)
            }
        }
    }

    class NetworkStateViewHolder (view: View) : RecyclerView.ViewHolder(view) {

        fun bind(networkState: NetworkState?) {
            if (networkState != null && networkState == NetworkState.LOADING) {
                itemView.progress.visibility = View.VISIBLE
            }
            else  {
                itemView.progress.visibility = View.GONE
            }
            if (networkState != null && networkState == NetworkState.ERROR) {
                itemView.errorMsg.visibility = View.VISIBLE
                itemView.errorMsg.text = networkState.msg
            }
            else if (networkState != null && networkState == NetworkState.ENDOFLIST) {
                itemView.errorMsg.visibility = View.VISIBLE
                itemView.errorMsg.text = networkState.msg
            }
            else {
                itemView.errorMsg.visibility = View.GONE
            }
        }
    }

    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_VIEW
        } else  {
            MOVIE_VIEW
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

}
