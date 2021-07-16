package com.akeshishi.moviex.features.movies.moviedetails.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akeshishi.moviex.base.extensions.loadImage
import com.akeshishi.moviex.databinding.ItemVideoBinding
import com.akeshishi.moviex.pojo.movies.Videos
import com.akeshishi.moviex.utils.YOUTUBE_THUMBNAIL

/**
 * The Adapter class to bind movie's trailers.
 *
 * @property videoList is the list of videos.
 * @property getData is the required data to create an intent in [MovieDetailFragment] in order to
 * navigate to Youtube.
 */
class MovieVideoAdapter(
    private val videoList: List<Videos>,
    private val getData: (String) -> Unit
) :
    RecyclerView.Adapter<MovieVideoAdapter.VideoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val viewBinding =
            ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoViewHolder(viewBinding)
        }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) =
        holder.bind(videoList[position])

    override fun getItemCount() = videoList.size

    /**
     * The ViewHolder class for [MovieVideoAdapter].
     *
     * @property viewBinding is the view to hold each item of data.
     */
    inner class VideoViewHolder(private val viewBinding: ItemVideoBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        /**
         * Binds data to view items.
         *
         * @param item is the item of data in the [Videos] response model.
         */
        fun bind(item: Videos) {
            item.let { video ->
                viewBinding.imgPoster.loadImage(YOUTUBE_THUMBNAIL + video.key + "/0.jpg")
                itemView.setOnClickListener { getData(video.key) }
            }
        }
    }
}
