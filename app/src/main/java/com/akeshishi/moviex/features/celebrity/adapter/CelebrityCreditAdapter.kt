package com.akeshishi.moviex.features.celebrity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.akeshishi.moviex.R
import com.akeshishi.moviex.base.extensions.loadImage
import com.akeshishi.moviex.base.extensions.makeVisible
import com.akeshishi.moviex.databinding.ItemSimilarBinding
import com.akeshishi.moviex.features.home.adapter.MovieListDiffUtilsCallback
import com.akeshishi.moviex.pojo.search.CombinedMovies
import com.akeshishi.moviex.utils.POSTER_BASE_URL

/**
 * The Adapter class to bind celebrities' movie credits.
 *
 * @property getData is the required data to navigate from [CelebrityCreditFragment] to [MovieDetailFragment].
 */
class CelebrityCreditAdapter(private val context: Context, private val getData: (Int) -> Unit) :
    PagingDataAdapter<CombinedMovies, CelebrityCreditAdapter.MovieViewHolder>(
        MovieListDiffUtilsCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val viewBinding =
            ItemSimilarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) =
        holder.movieBind(getItem(position))

    /**
     * The ViewHolder class for [CelebrityCreditAdapter].
     *
     * @property viewBinding is the view to hold each item of data.
     */
    inner class MovieViewHolder(private val viewBinding: ItemSimilarBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        /**
         * Binds data to view items.
         *
         * @param item is the item of data in the [CombinedMovies] response model.
         */
        fun movieBind(item: CombinedMovies?) {
            item?.let { movie ->
                viewBinding.imgPoster.loadImage(POSTER_BASE_URL + movie.posterPath,
                    R.drawable.poster_placeholder
                )

                viewBinding.txtTitle.text = if (movie.title.isNullOrEmpty()) movie.name
                else movie.title

                viewBinding.txtRelease.text =
                if (!movie.releaseDate.isNullOrEmpty() && movie.firstAirDate.isNullOrEmpty())
                     movie.releaseDate.substringBefore('-')
                else if (!movie.firstAirDate.isNullOrEmpty() && movie.releaseDate.isNullOrEmpty())
                     movie.firstAirDate.substringBefore('-')
                else  context.resources.getString(R.string.not_available)

                viewBinding.txtCharacter.makeVisible()

                viewBinding.txtCharacter.text = if (!movie.character.isNullOrEmpty())
                     movie.character
                else if (!movie.job.isNullOrEmpty()) movie.job
                else  context.resources.getString(R.string.not_available)

                viewBinding.rbRating.rating = movie.rating?.toFloat()!!.div(2)
                itemView.setOnClickListener { getData(movie.id) }
            }
        }
    }
}