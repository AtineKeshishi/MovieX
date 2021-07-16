package com.akeshishi.moviex.features.movies.moviedetails.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akeshishi.moviex.R
import com.akeshishi.moviex.base.extensions.loadImage
import com.akeshishi.moviex.databinding.ItemCastBinding
import com.akeshishi.moviex.pojo.movies.CreditDetails
import com.akeshishi.moviex.utils.POSTER_BASE_URL

/**
 * The Adapter class to bind Movie's cast and crew list.
 *
 * @property creditList is the list of cast or crew.
 * @property getData is the required data to navigate from [MovieDetailFragment] to [CastDetailFragment].
 */
class MovieCreditListAdapter(
    private val context: Context,
    private val creditList: List<CreditDetails>,
    private val getData: (Int) -> Unit
) : RecyclerView.Adapter<MovieCreditListAdapter.CastViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieCreditListAdapter.CastViewHolder {
        val viewBinding =
            ItemCastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CastViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) =
        holder.castBind(creditList[position])

    override fun getItemCount() = creditList.size

    /**
     * The ViewHolder class for [MovieCreditListAdapter].
     *
     * @property viewBinding is the view to hold each item of data.
     */
    inner class CastViewHolder(private val viewBinding: ItemCastBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        /**
         * Binds data to view items.
         *
         * @param item is the item of data in the [CreditDetails] response model.
         */
        fun castBind(item: CreditDetails?) {
            item?.let { cast ->
                viewBinding.actorName.text = cast.name
                viewBinding.characterName.text =
                    if (cast.character.isNullOrEmpty())
                        context.resources.getString(R.string.unknown)
                    else cast.character.substringBefore('/')

                viewBinding.profileImage.loadImage(
                    POSTER_BASE_URL + item.profilePath,
                    R.drawable.person_placeholder)
                itemView.setOnClickListener { getData(cast.id) }
            }
        }
    }
}
