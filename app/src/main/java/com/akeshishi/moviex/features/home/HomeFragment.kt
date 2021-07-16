package com.akeshishi.moviex.features.home

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.akeshishi.moviex.MainActivity
import com.akeshishi.moviex.R
import com.akeshishi.moviex.base.extensions.getErrorMessage
import com.akeshishi.moviex.base.extensions.makeGone
import com.akeshishi.moviex.base.extensions.makeVisible
import com.akeshishi.moviex.databinding.FragmentHomeBinding
import com.akeshishi.moviex.features.home.adapter.CelebrityListAdapter
import com.akeshishi.moviex.features.home.adapter.TrendingAdapter
import com.akeshishi.moviex.features.home.genres.adapter.GenresAdapter
import com.akeshishi.moviex.pojo.movies.Genre
import com.akeshishi.moviex.utils.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.IOException
import kotlin.system.exitProcess

/**
 * Access to this page is from [MainActivity].
 */
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModel()
    private val sharedPreferences: SharedPreferences by inject()
    private lateinit var viewBinding: FragmentHomeBinding
    private lateinit var trendingMovieAdapter: TrendingAdapter
    private lateinit var trendingTvAdapter: TrendingAdapter
    private lateinit var genresAdapter: GenresAdapter
    private lateinit var airingTodayAdapter: TrendingAdapter
    private lateinit var celebrityListAdapter: CelebrityListAdapter
    private var mediaType = MOVIE

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentHomeBinding.inflate(inflater)
        viewBinding.lifecycleOwner = viewLifecycleOwner
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.homeToolbar.txtTitle.text = getString(R.string.app_name)
        setUpMovieAdapter()
        setUpTvAdapter()
        setUpAiringAdapter()
        setUpCelebrityAdapter()
        loadMovieGenres()
        getCreditType()
        backPressed()
        setOnClickListener()
    }

    private fun setUpMovieAdapter() {
        trendingMovieAdapter = TrendingAdapter {
            val action =
                HomeFragmentDirections.homeFragmentToMovieDetailFragment(it)
            findNavController().navigate(action)
        }
        viewBinding.rvTrendingMovieList.adapter = trendingMovieAdapter
        loadMovieList()
    }

    private fun setUpTvAdapter() {
        trendingTvAdapter = TrendingAdapter {
            val action =
                HomeFragmentDirections.homeFragmentToShowDetailFragment(it)
            findNavController().navigate(action)
        }
        viewBinding.rvTrendingShowList.adapter = trendingTvAdapter
        loadTvList()
    }

    private fun setUpAiringAdapter() {
        airingTodayAdapter = TrendingAdapter {
            val action =
                HomeFragmentDirections.homeFragmentToShowDetailFragment(it)
            findNavController().navigate(action)
        }
        viewBinding.rvAiringShowList.adapter = airingTodayAdapter
        loadAiringList()
    }

    private fun setUpGenresAdapter(list: List<Genre>) {
        genresAdapter = GenresAdapter(list) { id, title ->
            val action =
                HomeFragmentDirections.homeFragmentToPopularGenresListFragment(id, title, mediaType)
            findNavController().navigate(action)
        }
        viewBinding.rvGenreList.adapter = genresAdapter

    }

    private fun setUpCelebrityAdapter() {
        celebrityListAdapter = CelebrityListAdapter {
            val action =
                HomeFragmentDirections.homeFragmentToCelebrityDetailFragment(it)
            findNavController().navigate(action)
        }
        viewBinding.rvPopularCelebrities.adapter = celebrityListAdapter
        loadPopularCelebrities()
    }

    private fun loadMovieList() {
        lifecycleScope.launch {
            viewModel.getTrendingMovies()
            viewModel.trendingMovieDataFlow?.collectLatest { trendingMovieAdapter.submitData(it) }
        }
        handleLoading()
    }

    private fun loadTvList() {
        lifecycleScope.launch {
            viewModel.getTrendingShows()
            viewModel.trendingShowDataFlow?.collectLatest { trendingTvAdapter.submitData(it) }
        }
    }

    private fun loadAiringList() {
        lifecycleScope.launch {
            viewModel.getAiringTodayList()
            viewModel.airingDataFlow?.collectLatest { airingTodayAdapter.submitData(it) }
        }
    }

    private fun loadPopularCelebrities() {
        lifecycleScope.launch {
            viewModel.getCelebrityList()
            viewModel.celebrityDataFlow?.collectLatest { celebrityListAdapter.submitData(it) }
        }
    }

    private fun loadMovieGenres() {
        viewModel.getMovieGenres(mediaType)
        viewModel.getMovieGenreLiveData().removeObservers(viewLifecycleOwner)
        viewModel.getMovieGenreLiveData().observe(viewLifecycleOwner) { response ->
            when (response) {
                is GeneralResponse.Loading -> viewBinding.prgGenreData.makeVisible()
                is GeneralResponse.Success -> {
                    viewBinding.prgGenreData.makeGone()
                    setUpGenresAdapter(response.data!!.genres)
                }
            }
        }
    }

    private fun handleLoading() {
        lifecycleScope.launch {
            trendingMovieAdapter.loadStateFlow.collectLatest {
                viewBinding.loadingPage.prgLoadingPage.isVisible = it.refresh is LoadState.Loading
                when (it.refresh) {
                    is LoadState.Error -> {
                        viewBinding.loadingPage.prgLoadingPage.makeGone()
                        when((it.refresh as LoadState.Error).error) {
                            is IOException -> viewBinding.networkErrorPage.networkErrorPage.makeVisible()
                            else -> getErrorMessage((it.refresh as LoadState.Error).error)
                        }
                    }
                }
            }
        }
    }

    private fun getCreditType() {
        sharedPreferences.edit().putString(MOVIE_TYPE, MOVIE).apply()
        sharedPreferences.edit().putString(TV_TYPE, TV).apply()
    }

    private fun setOnClickListener() {
        viewBinding.homeToolbar.imgDrawer.setOnClickListener {
            (requireActivity() as MainActivity).viewBinding.drawerLayout.openDrawer(
                GravityCompat.START
            )
        }
        viewBinding.txtMovieGenre.setOnClickListener {
            mediaType = MOVIE
            viewBinding.txtShowsGenre.setTextColor(ContextCompat.getColor(requireContext(), R.color.secondaryTextColor))
            viewBinding.txtMovieGenre.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorSecondary))
            loadMovieGenres()
        }
        viewBinding.txtShowsGenre.setOnClickListener {
            mediaType = TV
            viewBinding.txtMovieGenre.setTextColor(ContextCompat.getColor(requireContext(), R.color.secondaryTextColor))
            viewBinding.txtShowsGenre.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorSecondary))
            loadMovieGenres()
        }
        viewBinding.networkErrorPage.txtTryAgainBtn.setOnClickListener {
            viewBinding.networkErrorPage.networkErrorPage.makeGone()
            trendingMovieAdapter.retry()
            trendingTvAdapter.retry()
            airingTodayAdapter.retry()
            celebrityListAdapter.retry()

        }
    }

    private fun backPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() = exitProcess(0)
            }
        )
    }
}
