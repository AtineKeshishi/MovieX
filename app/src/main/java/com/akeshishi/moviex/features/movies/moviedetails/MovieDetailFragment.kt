package com.akeshishi.moviex.features.movies.moviedetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.akeshishi.moviex.R
import com.akeshishi.moviex.base.extensions.*
import com.akeshishi.moviex.databinding.FragmentMovieDetailsBinding
import com.akeshishi.moviex.features.movies.moviedetails.adapter.MovieCreditListAdapter
import com.akeshishi.moviex.features.movies.moviedetails.adapter.MovieVideoAdapter
import com.akeshishi.moviex.pojo.movies.CreditDetails
import com.akeshishi.moviex.pojo.movies.Videos
import com.akeshishi.moviex.utils.GeneralResponse
import com.akeshishi.moviex.utils.POSTER_BASE_URL
import com.akeshishi.moviex.utils.convertDate
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.IOException
import java.text.NumberFormat
import java.util.*

/**
 * This fragment contains detailed information about movies.
 * Access to this page is from the [MovieListFragment].
 */
class MovieDetailFragment : Fragment() {

    private val args: MovieDetailFragmentArgs by navArgs()
    private val viewModel: MovieDetailViewModel by viewModel { parametersOf(args.movieId) }
    private lateinit var viewBinding: FragmentMovieDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentMovieDetailsBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.lifecycleOwner = viewLifecycleOwner
        loadDetailData()
        loadCastListData()
        loadVideos()
        backPressed()
        setOnClickListener()
    }


    private fun loadDetailData() {
        viewModel.getMovieDetailData()
        viewModel.getMovieDetailLiveDate().removeObservers(viewLifecycleOwner)
        viewModel.getMovieDetailLiveDate().observe(viewLifecycleOwner) { response ->
            response.data?.let { detail ->
                viewBinding.collapsingToolbar.title = detail.title
                viewBinding.imgBackdrop.loadImage(
                    POSTER_BASE_URL +
                            if (!detail.backdropPath.isNullOrEmpty()) detail.backdropPath
                            else detail.posterPath
                )
                viewBinding.txtDetailGenre.text = detail.genres.joinToString { it.name }

                viewBinding.txtBudget.text = getString(
                    R.string.budget, NumberFormat.getCurrencyInstance(Locale.US)
                        .format(detail.budget).substringBefore('.')
                )
                viewBinding.txtRevenue.text = getString(
                    R.string.revenue, NumberFormat.getCurrencyInstance(Locale.US)
                        .format(detail.revenue).substringBefore('.')
                )

                viewBinding.txtRelease.text =
                    getString(R.string.release, convertDate(detail.releaseDate!!))

                viewBinding.txtRuntime.text =
                    getString(R.string.runtime, detail.runtime.toString()).plus(" minutes")

                viewBinding.txtCountry.text = getString(
                    R.string.countries,
                    detail.productionCountries.joinToString { it.name }
                )
                viewBinding.txtLanguage.text = getString(
                    R.string.languages,
                    detail.spokenLanguages.joinToString { it.englishName }
                )

                if (detail.homepage!!.isNotEmpty()) {
                    viewBinding.txtHomepage.makeVisible()
                    viewBinding.txtHomepage.text = getString(R.string.homepg, detail.homepage)
                }

                viewBinding.txtDetailOverview.text = detail.overview
            }

            when (response) {
                is GeneralResponse.Loading -> viewBinding.loadingPage.prgLoadingPage.makeVisible()
                is GeneralResponse.Success -> {
                    viewBinding.loadingPage.prgLoadingPage.makeGone()
                    viewBinding.networkErrorPage.networkErrorPage.makeGone()
                }
                is GeneralResponse.Error -> {
                    viewBinding.loadingPage.prgLoadingPage.makeGone()
                    when(response.cause){
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
                MovieDetailFragmentDirections.movieDetailFragmentToCastDetailFragment(it)
            findNavController().navigate(action)
        }
        viewBinding.rvCast.adapter = castAdapter
    }

    private fun setUpVideoAdapter(videoList: List<Videos>) {
        val videoAdapter = MovieVideoAdapter(videoList) { videoKey ->
            val viewIntent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(getString(R.string.youtube_link, videoKey))
            }
            startActivity(viewIntent)
        }
        viewBinding.rvVideo.adapter = videoAdapter
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

    private fun loadVideos() {
        viewModel.getVideoList()
        viewModel.getVideoListLiveData().removeObservers(viewLifecycleOwner)
        viewModel.getVideoListLiveData().observe(viewLifecycleOwner) {
            when (it) {
                is GeneralResponse.Success -> {
                    setUpVideoAdapter(it.data!!.results)
                    if (it.data.results.isNullOrEmpty()) {
                        viewBinding.txtVideo.makeVisible()
                        viewBinding.txtVideo.text = getString(R.string.not_available2)
                    }
                }
            }
        }
    }

    private fun openSimilarMovieListBottomSheet() {
        val action =
            MovieDetailFragmentDirections.movieDetailFragmentToSimilarMovieFragment(args.movieId)
        findNavController().navigate(action)
    }

    private fun setOnClickListener() {
        viewBinding.txtSimilarMovies.setOnClickListener { openSimilarMovieListBottomSheet() }
        viewBinding.detailToolbar.setOnClickListener { findNavController().navigateUp() }
        viewBinding.networkErrorPage.txtTryAgainBtn.setOnClickListener {
            loadDetailData()
            loadCastListData()
            loadVideos()
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
}