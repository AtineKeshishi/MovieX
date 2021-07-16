package com.akeshishi.moviex.features.home.genres.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akeshishi.moviex.databinding.ItemGenreBinding
import com.akeshishi.moviex.pojo.movies.Genre

/**
 * The Adapter class to bind popular genres.
 *
 * @property genreList is the list of genres.
 * @property getData is the required data to navigate from [HomeFragment] to [PopularGenresListFragment].
 */
class GenresAdapter(private val genreList: List<Genre>, private val getData: (Int, String) -> Unit) :
    RecyclerView.Adapter<GenresAdapter.GenresViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenresViewHolder {
        val viewBinding =
            ItemGenreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GenresViewHolder(viewBinding)
    }

    override fun getItemCount() = genreList.size

    override fun onBindViewHolder(holder: GenresViewHolder, position: Int) {
        holder.genresBind(genreList[position])
    }

    /**
     * he ViewHolder class for [GenresAdapter].
     *
     * @property viewBinding is the view to hold each item of data.
     */
    inner class GenresViewHolder(private val viewBinding: ItemGenreBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        /**
         * Binds data to view items.
         *
         * @param item is the item of data in the [Genre] response model.
         */
        fun genresBind(item: Genre) {
            item.let { genre ->
                viewBinding.txtGenreBtn.text = genre.name
                itemView.setOnClickListener { getData(genre.id, genre.name) }
            }
        }
    }
}