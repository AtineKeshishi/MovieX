package com.akeshishi.moviex.features.shows.showdetails.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akeshishi.moviex.R
import com.akeshishi.moviex.base.extensions.loadImage
import com.akeshishi.moviex.databinding.ItemSeasonListBinding
import com.akeshishi.moviex.pojo.shows.SeasonListResult
import com.akeshishi.moviex.utils.POSTER_BASE_URL

/**
 * The Adapter class to bind season list.
 *
 * @property getData is the required data to navigate from [ShowDetailFragment] to [SeasonListFragment].
 */
class SeasonListAdapter(
    private val context: Context,
    private val seasonList: List<SeasonListResult>,
    private val getData: (Int) -> Unit
) :
    RecyclerView.Adapter<SeasonListAdapter.SeasonViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SeasonViewHolder {
        val viewBinding =
            ItemSeasonListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SeasonViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: SeasonViewHolder, position: Int) {
        holder.bind(seasonList[position])
    }

    override fun getItemCount(): Int = seasonList.size

    /**
     * The ViewHolder class for [SeasonListAdapter].
     *
     * @property viewBinding is the view to hold each item of data.
     */
    inner class SeasonViewHolder(private val viewBinding: ItemSeasonListBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        /**
         * Binds data to view items.
         *
         * @param item is the item of data in the [SeasonListResult] response model.
         */
        fun bind(item: SeasonListResult) {
            item.let { season ->
                viewBinding.txtSeasonName.text = season.name
                viewBinding.txtEpisodeCount.text =
                    context.resources.getString(R.string.episodes, season.episodeCount.toString())
                    viewBinding.txtAirDate.text = season.airDate?.substringBefore('-')

                viewBinding.profileImage.loadImage(
                    POSTER_BASE_URL + season.posterPath,
                    R.drawable.poster_placeholder
                )
                itemView.setOnClickListener { getData(season.seasonNumber) }
            }
        }
    }
}