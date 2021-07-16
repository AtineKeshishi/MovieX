package com.akeshishi.moviex.features.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.akeshishi.moviex.R
import com.akeshishi.moviex.base.extensions.loadImage
import com.akeshishi.moviex.databinding.ItemTrendingListBinding
import com.akeshishi.moviex.pojo.search.CombinedMovies
import com.akeshishi.moviex.utils.POSTER_BASE_URL
import com.akeshishi.moviex.utils.TV

/**
 * The Adapter class to bind the trending movie list.
 *
 * @property getData is the required data to navigate from [HomeFragment] to [MovieDetailFragment]
 * and [ShowDetailFragment]
 */
class TrendingAdapter(private val getData: (Int) -> Unit) :
    PagingDataAdapter<CombinedMovies, TrendingAdapter.TrendingViewHolder>(MovieListDiffUtilsCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingViewHolder {
        val viewBinding = ItemTrendingListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TrendingViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: TrendingViewHolder, position: Int) =
        holder.bind(getItem(position))


    /**
     * The ViewHolder class for [TrendingAdapter].
     *
     * @property viewBinding is the view to hold each item of data.
     */
    inner class TrendingViewHolder(private val viewBinding: ItemTrendingListBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        /**
         * Binds data to view items.
         *
         * @param item is the item of data in the [CombinedMovies] response model.
         */
        fun bind(item: CombinedMovies?) {
            item?.let { movie ->
                viewBinding.txtTitle.text =
                    if (movie.mediaType == TV) movie.name else movie.title

                if (movie.title.isNullOrEmpty())
                    viewBinding.txtTitle.text = movie.name

                viewBinding.imgPoster.loadImage(
                    POSTER_BASE_URL + movie.posterPath,
                    R.drawable.poster_placeholder
                )
                itemView.setOnClickListener { getData(movie.id) }
            }
        }
    }
}