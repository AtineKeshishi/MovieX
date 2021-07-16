package com.akeshishi.moviex.features.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.akeshishi.moviex.R
import com.akeshishi.moviex.base.extensions.loadImage
import com.akeshishi.moviex.databinding.ItemCelebrityBinding
import com.akeshishi.moviex.pojo.celebrity.PopularCelebrities
import com.akeshishi.moviex.utils.POSTER_BASE_URL

/**
 * The Adapter class to bind celebrities' list.
 *
 * @property getData is the required data to navigate from [HomeFragment] to [CelebrityDetailFragment].
 */
class CelebrityListAdapter(private val getData: (Int) -> Unit) :
    PagingDataAdapter<PopularCelebrities, CelebrityListAdapter.CelebrityViewHolder>(
        CelebrityListDiffUtilsCallback()
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CelebrityViewHolder {
        val viewBinding = ItemCelebrityBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CelebrityViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: CelebrityViewHolder, position: Int) =
        holder.bind(getItem(position))

    /**
     * The ViewHolder class for [CelebrityListAdapter].
     *
     * @property viewBinding is the view to hold each item of data.
     */
    inner class CelebrityViewHolder(private val viewBinding: ItemCelebrityBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        /**
         * Binds data to view items.
         *
         * @param item is the item of data in the [PopularCelebrities] response model.
         */
        fun bind(item: PopularCelebrities?) {
            item?.let { celebrity ->
                viewBinding.txtCelebrityName.text =
                    celebrity.name.replace(" ", System.getProperty("line.separator"))
                viewBinding.imgProfileImage.loadImage(
                    POSTER_BASE_URL + celebrity.profilePath,
                    R.drawable.person_placeholder
                )
                itemView.setOnClickListener { getData(celebrity.id) }
            }
        }
    }
}

/**
 * DiffUtil Class for [CelebrityListAdapter] that checks [PopularCelebrities.id] to avoid duplicated
 * items in the [PopularCelebrities] list.
 */
class CelebrityListDiffUtilsCallback : DiffUtil.ItemCallback<PopularCelebrities>() {

    override fun areItemsTheSame(oldItem: PopularCelebrities, newItem: PopularCelebrities): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PopularCelebrities, newItem: PopularCelebrities): Boolean {
        return oldItem == newItem
    }
}