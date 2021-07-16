package com.akeshishi.moviex.features.search.adapter.viewholder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.akeshishi.moviex.R
import com.akeshishi.moviex.base.extensions.loadImage
import com.akeshishi.moviex.databinding.ItemCelebrityListBinding
import com.akeshishi.moviex.pojo.search.CombinedMovies
import com.akeshishi.moviex.utils.POSTER_BASE_URL

/**
 * The ViewHolder class for [SearchAdapter].
 *
 * @property viewBinding is the view to hold each item of data.
 * @property getData is the required data to navigate from [SearchFragment] to [CelebrityDetailFragment].
 */
class CelebrityViewHolder(
    private val viewBinding: ItemCelebrityListBinding,
    private val context: Context,
    private val getData: (Int, String) -> Unit
) :
    RecyclerView.ViewHolder(viewBinding.root) {

    /**
     * Binds data to view items.
     *
     * @param item is the item of data in the [CombinedMovies] response model.
     */
    fun celebrityBind(item: CombinedMovies?) {
        item?.let { celebrity ->
            viewBinding.txtName.text = celebrity.name
            viewBinding.txtKnownFor.text = context.resources.getString(
                R.string.occupation,
                celebrity.knownFor?.map {
                    if (it.mediaType == "movie") it.title
                    else it.name
                }?.joinToString()
            )
            viewBinding.txtPopularity.text = context.resources.getString(
                R.string.popularity,
                celebrity.popularity.toString()
            )
            viewBinding.profileImage.loadImage(
                POSTER_BASE_URL + celebrity.profilePath,
                R.drawable.person_placeholder
            )
            itemView.setOnClickListener { getData(celebrity.id, celebrity.mediaType!!) }
        }
    }
}