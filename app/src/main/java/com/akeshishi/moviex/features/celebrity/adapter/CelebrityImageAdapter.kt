package com.akeshishi.moviex.features.celebrity.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akeshishi.moviex.R
import com.akeshishi.moviex.base.extensions.loadImage
import com.akeshishi.moviex.base.extensions.makeGone
import com.akeshishi.moviex.databinding.ItemCastBinding
import com.akeshishi.moviex.pojo.celebrity.Profile
import com.akeshishi.moviex.utils.POSTER_BASE_URL

/**
 * The Adapter class to bind celebrities' images.
 *
 * @property imageList is the list of images.
 * @property getData is the required data to create an intent in [CelebrityBiographyFragment] to display the image.
 */
class CelebrityImageAdapter(
    private val imageList: List<Profile>,
    private val getData: (String) -> Unit
) : RecyclerView.Adapter<CelebrityImageAdapter.ActorImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorImageViewHolder {
        val viewBinding =
            ItemCastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActorImageViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ActorImageViewHolder, position: Int) =
        holder.bind(imageList[position])

    override fun getItemCount(): Int = imageList.size

    /**
     * The ViewHolder class for [CelebrityImageAdapter].
     *
     * @property viewBinding is the view to hold each item of data.
     */
    inner class ActorImageViewHolder(private val viewBinding: ItemCastBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        /**
         * Binds data to view items.
         *
         * @param image is the item of data in the [Profile] response model.
         */
        fun bind(image: Profile) {
            viewBinding.profileImage.loadImage(
                POSTER_BASE_URL + image.filePath,
                R.drawable.person_placeholder
            )
            viewBinding.characterName.makeGone()
            viewBinding.actorName.makeGone()
            itemView.setOnClickListener { getData(image.filePath) }
        }
    }
}
