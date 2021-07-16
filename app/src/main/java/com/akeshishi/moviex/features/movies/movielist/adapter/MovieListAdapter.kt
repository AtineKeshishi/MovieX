package com.akeshishi.moviex.features.movies.movielist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.akeshishi.moviex.R
import com.akeshishi.moviex.base.extensions.loadImage
import com.akeshishi.moviex.databinding.ItemMovieBinding
import com.akeshishi.moviex.features.home.adapter.MovieListDiffUtilsCallback
import com.akeshishi.moviex.pojo.search.CombinedMovies
import com.akeshishi.moviex.utils.POSTER_BASE_URL
import com.akeshishi.moviex.utils.convertDate

/**
 * The Adapter class to bind the movie list.
 *
 * @property getData is the required data to navigate from [MovieListFragment] to [MovieDetailFragment],
 * and from [ShowListFragment] to [ShowDetailFragment].
 */
class MovieListAdapter(private val context: Context, private val getData: (Int) -> Unit) :
    PagingDataAdapter<CombinedMovies, MovieListAdapter.MovieViewHolder>(MovieListDiffUtilsCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val viewBinding =
            ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) =
        holder.bind(getItem(position))

    /**
     * The ViewHolder class for [MovieListAdapter].
     *
     * @property viewBinding is the view to hold each item of data.
     */
    inner class MovieViewHolder(private val viewBinding: ItemMovieBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        /**
         * Binds data to view items.
         *
         * @param item is the item of data in the [CombinedMovies] response model.
         */
        fun bind(item: CombinedMovies?) {
            item?.let { movie ->
                viewBinding.txtTitle.text =
                    when {
                        movie.name.isNullOrEmpty() -> movie.title
                        movie.title.isNullOrEmpty() -> movie.name
                        else -> context.resources.getString(R.string.not_available)
                    }

                viewBinding.rbRating.rating = movie.rating!!.toFloat().div(2)

                viewBinding.txtRelease.text =
                    when {
                        movie.firstAirDate.isNullOrEmpty() -> context.resources.getString(
                            R.string.release, convertDate(movie.releaseDate!!))
                        movie.releaseDate.isNullOrEmpty() -> context.resources.getString(
                            R.string.first_air_date, convertDate(movie.firstAirDate))
                        else -> context.resources.getString(R.string.not_available)
                    }

                viewBinding.txtOverview.text =
                    if (!movie.overview.isNullOrEmpty()) movie.overview
                    else context.resources.getString(R.string.info_not_available)
                viewBinding.imgPoster.loadImage(
                    POSTER_BASE_URL + movie.posterPath, R.drawable.poster_placeholder)
                itemView.setOnClickListener { getData(movie.id) }
            }
        }
    }
}