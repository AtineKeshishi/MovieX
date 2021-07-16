package com.akeshishi.moviex.features.search.adapter.viewholder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.akeshishi.moviex.R
import com.akeshishi.moviex.base.extensions.loadImage
import com.akeshishi.moviex.base.extensions.makeVisible
import com.akeshishi.moviex.databinding.ItemSimilarBinding
import com.akeshishi.moviex.pojo.search.CombinedMovies
import com.akeshishi.moviex.utils.POSTER_BASE_URL
import com.akeshishi.moviex.utils.convertDate

/**
 * The ViewHolder class for [SearchAdapter].
 *
 * @property viewBinding is the view to hold each item of data.
 * @property getData is the required data to navigate from [SearchFragment] to [MovieDetailFragment].
 */
class MovieViewHolder(
    private val viewBinding: ItemSimilarBinding,
    private val context: Context,
    private val getData: (Int, String) -> Unit
) :
    RecyclerView.ViewHolder(viewBinding.root) {

    /**
     * Binds data to view items.
     *
     * @param item is the item of data in the [CombinedMovies] response model.
     */
    fun movieBind(item: CombinedMovies?) {
        item?.let { movie ->
            viewBinding.txtTitle.text = movie.title
            viewBinding.txtRelease.text = context.resources.getString(
                R.string.release,
                movie.releaseDate?.let { convertDate(it) }
            )
            viewBinding.rbRating.rating = movie.rating?.toFloat()!!.div(2)
            viewBinding.imgPoster.loadImage(
                POSTER_BASE_URL + movie.posterPath,
                R.drawable.poster_placeholder
            )
            viewBinding.imgIcon.makeVisible()
            viewBinding.imgIcon.setImageDrawable(context.resources.getDrawable(R.drawable.ic_movies, null))
            itemView.setOnClickListener { getData(movie.id, movie.mediaType!!) }
        }
    }
}