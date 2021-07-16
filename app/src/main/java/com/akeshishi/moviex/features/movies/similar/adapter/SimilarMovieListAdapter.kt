package com.akeshishi.moviex.features.movies.similar.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.akeshishi.moviex.R
import com.akeshishi.moviex.base.extensions.loadImage
import com.akeshishi.moviex.databinding.ItemSimilarBinding
import com.akeshishi.moviex.features.home.adapter.MovieListDiffUtilsCallback
import com.akeshishi.moviex.pojo.search.CombinedMovies
import com.akeshishi.moviex.utils.POSTER_BASE_URL
import com.akeshishi.moviex.utils.convertDate

/**
 * The Adapter class to bind similar movie list.
 *
 * @property getData is the required data to navigate from [MovieDitailFragment] to [SimilarMovieFragment].
 */
class SimilarMovieListAdapter(
    private val context: Context,
    private val getData: (Int) -> Unit
) :
    PagingDataAdapter<CombinedMovies, SimilarMovieListAdapter.SimilarViewHolder>(
        MovieListDiffUtilsCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarViewHolder {
        val viewBinding =
            ItemSimilarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SimilarViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: SimilarViewHolder, position: Int) =
        holder.bind(getItem(position))

    /**
     * The ViewHolder class for [SimilarMovieListAdapter].
     *
     * @property viewBinding is the view to hold each item of data.
     */
    inner class SimilarViewHolder(private val viewBinding: ItemSimilarBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        /**
         * Binds data to view items.
         *
         * @param item is the item of data in the [CombinedMovies] response model.
         */
        fun bind(item: CombinedMovies?) {
            item?.let { movie ->
                viewBinding.txtTitle.text = movie.title
                if (movie.releaseDate!!.isNotEmpty())
                    viewBinding.txtRelease.text = context.resources.getString(
                        R.string.release, convertDate(movie.releaseDate)
                    )

                viewBinding.rbRating.rating = movie.rating!!.toFloat().div(2)

                viewBinding.imgPoster.loadImage(
                    POSTER_BASE_URL + movie.posterPath,
                    R.drawable.poster_placeholder
                )

                itemView.setOnClickListener { getData(movie.id) }
            }
        }
    }
}

