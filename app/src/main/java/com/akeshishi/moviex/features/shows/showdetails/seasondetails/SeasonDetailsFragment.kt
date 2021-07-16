package com.akeshishi.moviex.features.shows.showdetails.seasondetails

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.akeshishi.moviex.R
import com.akeshishi.moviex.base.extensions.*
import com.akeshishi.moviex.databinding.FragmentSeasonDetailsBinding
import com.akeshishi.moviex.features.shows.showdetails.seasondetails.adapter.SeasonDetailsListAdapter
import com.akeshishi.moviex.pojo.shows.EpisodeListResult
import com.akeshishi.moviex.utils.GeneralResponse
import com.akeshishi.moviex.utils.POSTER_BASE_URL
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
 * This fragment contains the list of episodes every season contains.
 * Access to this page is from the [ShowDetailFragment].
 */
class SeasonDetailsFragment : BottomSheetDialogFragment() {

    private val args: SeasonDetailsFragmentArgs by navArgs()
    private lateinit var viewBinding: FragmentSeasonDetailsBinding
    private lateinit var episodeAdapter: SeasonDetailsListAdapter
    private val viewModel: SeasonDetailsViewModel by viewModel {
        parametersOf(
            args.showId, args.seasonNumber
        )
    }

    override fun onCreateView(
        layoutInflater: LayoutInflater,
        parent: ViewGroup?,
        saved: Bundle?
    ): View {
        viewBinding =
            FragmentSeasonDetailsBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.lifecycleOwner = viewLifecycleOwner
        loadDetailData()
    }

    override fun getTheme(): Int = R.style.CustomBottomSheetDialog

    private fun loadDetailData() {
        viewModel.getSeasonDetailsData()
        viewModel.getSeasonLiveDate().removeObservers(viewLifecycleOwner)
        viewModel.getSeasonLiveDate().observe(viewLifecycleOwner) { response ->
            response.data?.let { detail ->
                viewBinding.header.text = detail.name
                viewBinding.imgSeasonPoster.loadImage(
                    POSTER_BASE_URL + detail.posterPath,
                    R.drawable.poster_placeholder
                )

                viewBinding.txtSeasonOverview.movementMethod = ScrollingMovementMethod()
                viewBinding.txtSeasonOverview.text =
                    if (detail.overview.isNotEmpty()) detail.overview
                    else getString(R.string.no_overview)

            }

            when (response) {
                is GeneralResponse.Loading -> {
                    viewBinding.prgData.makeVisible()
                }
                is GeneralResponse.Success -> {
                    viewBinding.prgData.makeGone()
                    setUpAdapter(response.data!!.episodes)
                }
                is GeneralResponse.Error -> {
                    viewBinding.prgData.makeGone()
                    viewBinding.notFound.makeVisible()
                    viewBinding.notFound.text = getString(R.string.oops)
                    getErrorMessage(response.cause)
                }
            }
        }
    }

    private fun setUpAdapter(episodeList: List<EpisodeListResult>) {
        episodeAdapter = SeasonDetailsListAdapter(requireContext(), episodeList) {
            if (episodeList[it].overview.isEmpty() && episodeList[it].guestStars.isEmpty())
                requireView().snackBar(getString(R.string.info_not_available))
            else {
                val action =
                    SeasonDetailsFragmentDirections.seasonDetailsFragmentToEpisodeDetailsFragment(
                        args.showId, args.seasonNumber, it
                    )
                findNavController().navigate(action)
            }
        }
        viewBinding.rvEpisodeList.adapter = episodeAdapter
    }
}