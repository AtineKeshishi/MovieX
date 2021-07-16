package com.akeshishi.moviex.features.shows.showdetails.episodedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.akeshishi.moviex.R
import com.akeshishi.moviex.base.extensions.getErrorMessage
import com.akeshishi.moviex.base.extensions.loadImage
import com.akeshishi.moviex.base.extensions.makeGone
import com.akeshishi.moviex.base.extensions.makeVisible
import com.akeshishi.moviex.databinding.FragmentEpisodeDetailsBinding
import com.akeshishi.moviex.features.movies.moviedetails.adapter.MovieCreditListAdapter
import com.akeshishi.moviex.pojo.movies.CreditDetails
import com.akeshishi.moviex.utils.GeneralResponse
import com.akeshishi.moviex.utils.POSTER_BASE_URL
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.IOException

/**
 * This fragment contains detailed information about every episode.
 * Access to this page is from the [EpisodeListFragment].
 */
class EpisodeDetailsFragment : Fragment() {

    private val args: EpisodeDetailsFragmentArgs by navArgs()
    private lateinit var viewBinding: FragmentEpisodeDetailsBinding
    private val viewModel: EpisodeDetailsViewModel by viewModel {
        parametersOf(args.showId, args.seasonNumber, args.episodeNumber)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentEpisodeDetailsBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.lifecycleOwner = viewLifecycleOwner

        loadDetailData()
        setOnClickListener()
    }

    private fun loadDetailData() {
        viewModel.getEpisodeDetailData()
        viewModel.getEpisodeDetailLiveDate().removeObservers(viewLifecycleOwner)
        viewModel.getEpisodeDetailLiveDate().observe(viewLifecycleOwner) { response ->
            response.data?.let { episode ->
                viewBinding.collapsingToolbar.title = episode.name
                viewBinding.imgBackdrop.loadImage(POSTER_BASE_URL + episode.stillPath)
                viewBinding.txtDetailOverview.text =
                    if (episode.overview.isNotEmpty()) episode.overview
                    else getString(R.string.no_overview)
            }
            when (response) {
                is GeneralResponse.Loading -> {
                    viewBinding.loadingPage.prgLoadingPage.makeVisible()
                }
                is GeneralResponse.Success -> {
                    viewBinding.loadingPage.prgLoadingPage.makeGone()
                    viewBinding.networkErrorPage.networkErrorPage.makeGone()
                    if (response.data?.crew.isNullOrEmpty()) {
                        viewBinding.txtNotAvailable.makeVisible()
                        viewBinding.txtNotAvailable.text = getString(R.string.info_not_available)
                    } else setUpCrewAdapter(response.data!!.crew)

                    if (response.data?.guestStars.isNullOrEmpty()) {
                        viewBinding.txtNotGuestAvailable.makeVisible()
                        viewBinding.txtNotGuestAvailable.text =
                            getString(R.string.info_not_available)
                    } else setUpGuestStarAdapter(response.data!!.guestStars)
                }
                is GeneralResponse.Error -> {
                    viewBinding.loadingPage.prgLoadingPage.makeGone()
                    when (response.cause){
                        is IOException -> viewBinding.networkErrorPage.networkErrorPage.makeVisible()
                        else -> getErrorMessage(response.cause)
                    }

                }
            }
        }
    }

    private fun setUpCrewAdapter(castList: List<CreditDetails>) {
        val castAdapter = MovieCreditListAdapter(requireContext(), castList) {
            val action =
                EpisodeDetailsFragmentDirections.episodeDetailsFragmentToCelebrityDetailFragment(it)
            findNavController().navigate(action)
        }
        viewBinding.rvCrew.adapter = castAdapter
    }

    private fun setUpGuestStarAdapter(castList: List<CreditDetails>) {
        val castAdapter = MovieCreditListAdapter(requireContext(), castList) {
            val action =
                EpisodeDetailsFragmentDirections.episodeDetailsFragmentToCelebrityDetailFragment(
                    it
                )
            findNavController().navigate(action)
        }
        viewBinding.rvGuestStar.adapter = castAdapter
    }

    private fun setOnClickListener() {
        viewBinding.detailToolbar.setOnClickListener { findNavController().navigateUp() }
        viewBinding.networkErrorPage.txtTryAgainBtn.setOnClickListener { loadDetailData() }
    }
}