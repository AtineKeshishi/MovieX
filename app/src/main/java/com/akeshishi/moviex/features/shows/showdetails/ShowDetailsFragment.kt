package com.akeshishi.moviex.features.shows.showdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.akeshishi.moviex.R
import com.akeshishi.moviex.base.extensions.*
import com.akeshishi.moviex.databinding.FragmentShowDetailsBinding
import com.akeshishi.moviex.features.movies.moviedetails.adapter.MovieCreditListAdapter
import com.akeshishi.moviex.features.shows.showdetails.adapter.CreatorListAdapter
import com.akeshishi.moviex.features.shows.showdetails.adapter.SeasonListAdapter
import com.akeshishi.moviex.pojo.movies.CreditDetails
import com.akeshishi.moviex.pojo.shows.Creators
import com.akeshishi.moviex.pojo.shows.SeasonListResult
import com.akeshishi.moviex.utils.GeneralResponse
import com.akeshishi.moviex.utils.POSTER_BASE_URL
import com.akeshishi.moviex.utils.convertDate
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.IOException

/**
 * This fragment contains detailed information about tv shows.
 * Access to this page is from the [ShowListFragment].
 */
class ShowDetailsFragment : Fragment() {
    private val args: ShowDetailsFragmentArgs by navArgs()
    private val viewModel: ShowDetailsViewModel by viewModel { parametersOf(args.showId) }
    private lateinit var viewBinding: FragmentShowDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentShowDetailsBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.lifecycleOwner = viewLifecycleOwner
        loadDetailData()
        loadCastListData()
        setOnClickListener()
        backPressed()
    }

    private fun loadDetailData() {
        viewModel.getShowDetailData()
        viewModel.getShowDetailsLiveData().removeObservers(viewLifecycleOwner)
        viewModel.getShowDetailsLiveData().observe(viewLifecycleOwner) { response ->
            response.data?.let { show ->
                viewBinding.collapsingToolbar.title = show.name

                viewBinding.imgBackdrop.loadImage(
                    POSTER_BASE_URL +
                            if (!show.backdropPath.isNullOrEmpty()) show.backdropPath
                            else show.posterPath
                )

                viewBinding.txtDetailGenre.text = show.genres.joinToString { it.name }

                viewBinding.txtNetwork.text =
                    getString(R.string.network, show.networks.joinToString { it.name })

                viewBinding.txtRuntime.text =
                    getString(R.string.runtime, show.episodeRunTime.joinToString()).plus(" minutes")

                viewBinding.txtFirstAir.text =
                    getString(R.string.first_air, convertDate(show.firstAirDate))

                viewBinding.txtLastAir.text =
                    getString(R.string.last_air,
                        show.lastAirDate?.let { convertDate(it) }
                            ?: getString(R.string.not_available))

                viewBinding.txtSeasonCount.text =
                    getString(R.string.season_count, show.seasons.size.toString())

                viewBinding.txtCountry.text = getString(
                    R.string.countries,
                    show.productionCountries.joinToString { it.name }
                )
                viewBinding.txtLanguage.text = getString(
                    R.string.languages,
                    show.spokenLanguages.joinToString { it.englishName }
                )

                if (show.homepage!!.isNotEmpty()) {
                    viewBinding.txtHomepage.makeVisible()
                    viewBinding.txtHomepage.text = getString(R.string.homepg, show.homepage)
                }

                viewBinding.txtDetailOverview.text =
                    if (show.overview.isNullOrEmpty()) getString(R.string.no_overview)
                    else show.overview
            }

            when (response) {
                is GeneralResponse.Loading -> {
                    viewBinding.loadingPage.prgLoadingPage.makeVisible()
                }
                is GeneralResponse.Success -> {
                    viewBinding.loadingPage.prgLoadingPage.makeGone()
                    viewBinding.networkErrorPage.networkErrorPage.makeGone()
                    setUpSeasonListAdapter(response.data!!.seasons)
                    setUpCreatorAdapter(response.data.createdBy)
                }
                is GeneralResponse.Error -> {
                    viewBinding.loadingPage.prgLoadingPage.makeGone()
                    when (response.cause) {
                        is IOException -> viewBinding.networkErrorPage.networkErrorPage.makeVisible()
                        else -> getErrorMessage(response.cause)
                    }
                }
            }
        }
    }

    private fun setUpCastAdapter(castList: List<CreditDetails>) {
        val castAdapter = MovieCreditListAdapter(requireContext(), castList) {
            val action =
                ShowDetailsFragmentDirections.showDetailFragmentToCastDetailFragment(it)
            findNavController().navigate(action)
        }
        viewBinding.rvCast.adapter = castAdapter
    }

    private fun loadCastListData() {
        viewModel.getCastList()
        viewModel.getCastListLiveData().removeObservers(viewLifecycleOwner)
        viewModel.getCastListLiveData().observe(viewLifecycleOwner) {
            when (it) {
                is GeneralResponse.Success -> setUpCastAdapter(it.data!!.cast)
            }
        }
    }

    private fun setUpSeasonListAdapter(seasonList: List<SeasonListResult>) {
        val actorCreditAdapter = SeasonListAdapter(requireContext(), seasonList)
        {
            val action =
                ShowDetailsFragmentDirections.showDetailFragmentToEpisodeListFragment(
                    args.showId,
                    it
                )
            findNavController().navigate(action)
        }
        viewBinding.rvSeasons.addItemDecoration(
            DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL)
        )
        viewBinding.rvSeasons.adapter = actorCreditAdapter
    }

    private fun setUpCreatorAdapter(creator: List<Creators>) {
        if (creator.isNullOrEmpty())
            viewBinding.txtCreator.apply {
                makeVisible()
                text = getString(R.string.info_not_available)
            }
        else {
            val actorCreditAdapter = CreatorListAdapter(creator) {
                val action =
                    ShowDetailsFragmentDirections.showDetailFragmentToCastDetailFragment(it)
                findNavController().navigate(action)
            }
            viewBinding.rvCreator.adapter = actorCreditAdapter
        }
    }

    private fun backPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireView().hideKeyboard()
                    findNavController().navigateUp()
                }
            }
        )
    }

    private fun setOnClickListener() {
        viewBinding.showDetailToolbar.setOnClickListener { findNavController().navigateUp() }
        viewBinding.networkErrorPage.txtTryAgainBtn.setOnClickListener {
            loadDetailData()
            loadCastListData()
        }
    }
}