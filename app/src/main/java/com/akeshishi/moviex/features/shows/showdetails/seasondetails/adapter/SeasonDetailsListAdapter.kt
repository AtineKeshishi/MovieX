package com.akeshishi.moviex.features.shows.showdetails.seasondetails.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akeshishi.moviex.R
import com.akeshishi.moviex.base.extensions.loadImage
import com.akeshishi.moviex.databinding.ItemEpisodeBinding
import com.akeshishi.moviex.pojo.shows.EpisodeListResult
import com.akeshishi.moviex.utils.POSTER_BASE_URL
import com.akeshishi.moviex.utils.convertDate

/**
 * The Adapter class to bind episode list.
 *
 * @property getData is the required data to navigate from [SeasonListFragment] to [EpisodeDetailsFragment].
 */
class SeasonDetailsListAdapter(
    private val context: Context,
    private val episodeList: List<EpisodeListResult>,
    private val getData: (Int) -> Unit
) :
    RecyclerView.Adapter<SeasonDetailsListAdapter.EpisodeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        val viewBinding =
            ItemEpisodeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EpisodeViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        holder.bind(episodeList[position])
    }

    override fun getItemCount(): Int = episodeList.size

    /**
     * The ViewHolder class for [SeasonDetailsListAdapter].
     *
     * @property viewBinding is the view to hold each item of data.
     */
    inner class EpisodeViewHolder(private val viewBinding: ItemEpisodeBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        /**
         * Binds data to view items.
         *
         * @param item is the item of data in the [EpisodeListResult] response model.
         */
        fun bind(item: EpisodeListResult) {
            item.let { episode ->
                viewBinding.txtTitle.text = episode.episodeNumber.toString() + ". " + episode.name
                if (episode.airDate.isNotEmpty())
                    viewBinding.txtRelease.text =
                        context.resources.getString(R.string.air_date, convertDate(episode.airDate))
                viewBinding.imgPoster.loadImage(
                    POSTER_BASE_URL + episode.stillPath,
                    R.drawable.episode_placeholder
                )
                itemView.setOnClickListener { getData(episode.episodeNumber) }
            }
        }
    }
}