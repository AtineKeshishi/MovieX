package com.akeshishi.moviex.features.shows.showdetails.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akeshishi.moviex.R
import com.akeshishi.moviex.base.extensions.loadImage
import com.akeshishi.moviex.base.extensions.makeGone
import com.akeshishi.moviex.databinding.ItemCastBinding
import com.akeshishi.moviex.pojo.shows.Creators
import com.akeshishi.moviex.utils.POSTER_BASE_URL

/**
 * The Adapter class to bind show's creators list.
 *
 * @property getData is the required data to navigate from [ShowDetailFragment] to [CelebrityDetailFragment].
 */
class CreatorListAdapter(
    private val creatorList: List<Creators>,
    private val getData: (Int) -> Unit
) : RecyclerView.Adapter<CreatorListAdapter.CreatorViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CreatorListAdapter.CreatorViewHolder {
        val viewBinding =
            ItemCastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CreatorViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: CreatorViewHolder, position: Int) =
        holder.bind(creatorList[position])

    override fun getItemCount(): Int = creatorList.size

    /**
     * The ViewHolder class for [CreatorListAdapter].
     *
     * @property viewBinding is the view to hold each item of data.
     */
    inner class CreatorViewHolder(private val viewBinding: ItemCastBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        /**
         * Binds data to view items.
         *
         * @param item is the item of data in the [Creators] response model.
         */
        fun bind(item: Creators?) {
            item?.let { celebrity ->
                viewBinding.profileImage.loadImage(
                    POSTER_BASE_URL + celebrity.profilePath,
                    R.drawable.person_placeholder
                )
                viewBinding.characterName.text = celebrity.name
                viewBinding.actorName.makeGone()
                itemView.setOnClickListener { getData(celebrity.id) }
            }
        }
    }
}