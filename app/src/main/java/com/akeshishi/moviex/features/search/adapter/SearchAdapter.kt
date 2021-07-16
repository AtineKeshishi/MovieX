package com.akeshishi.moviex.features.search.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.akeshishi.moviex.databinding.ItemCelebrityListBinding
import com.akeshishi.moviex.databinding.ItemSimilarBinding
import com.akeshishi.moviex.features.home.adapter.MovieListDiffUtilsCallback
import com.akeshishi.moviex.features.search.adapter.viewholder.CelebrityViewHolder
import com.akeshishi.moviex.features.search.adapter.viewholder.MovieViewHolder
import com.akeshishi.moviex.features.search.adapter.viewholder.ShowViewHolder
import com.akeshishi.moviex.pojo.search.CombinedMovies
import com.akeshishi.moviex.utils.MOVIE
import com.akeshishi.moviex.utils.TV

const val MOVIE_TYPE = 0
const val TV_TYPE = 1
const val CELEBRITY_TYPE = 2

/**
 * The Adapter class to bind search result list.
 *
 * @property getData is the required data to navigate from [SearchFragment] to [MovieDetailFragment],
 * [ShowDetailFragment] or [CelebrityDetailFragment].
 */
class SearchAdapter(
    private val context: Context,
    private val getData: (Int, String) -> Unit
) : PagingDataAdapter<CombinedMovies, RecyclerView.ViewHolder>(MovieListDiffUtilsCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            MOVIE_TYPE -> {
                val movieViewBinding = ItemSimilarBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return MovieViewHolder(movieViewBinding, context, getData)
            }
            TV_TYPE -> {
                val tvViewBinding = ItemSimilarBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ShowViewHolder(tvViewBinding, context, getData)
            }
            else -> {
                val celebrityViewBinding = ItemCelebrityListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return CelebrityViewHolder(celebrityViewBinding, context, getData)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is MovieViewHolder -> holder.movieBind(item)
            is ShowViewHolder -> holder.tvBind(item)
            is CelebrityViewHolder -> holder.celebrityBind(item)
        }
    }


    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)?.mediaType) {
            MOVIE -> MOVIE_TYPE
            TV -> TV_TYPE
            else -> CELEBRITY_TYPE
        }
    }
}

