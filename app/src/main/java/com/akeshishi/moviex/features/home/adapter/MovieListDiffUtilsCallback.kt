package com.akeshishi.moviex.features.home.adapter

import androidx.recyclerview.widget.DiffUtil
import com.akeshishi.moviex.pojo.search.CombinedMovies

/**
 * DiffUtil Class that checks [CombinedMovies.id] to avoid duplicated items in the [CombinedMovies] list.
 */
class MovieListDiffUtilsCallback : DiffUtil.ItemCallback<CombinedMovies>() {

    override fun areItemsTheSame(oldItem: CombinedMovies, newItem: CombinedMovies): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CombinedMovies, newItem: CombinedMovies): Boolean {
        return oldItem == newItem
    }
}