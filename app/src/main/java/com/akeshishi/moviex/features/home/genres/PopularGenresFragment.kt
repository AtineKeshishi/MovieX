package com.akeshishi.moviex.features.home.genres

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akeshishi.moviex.base.extensions.getErrorMessage
import com.akeshishi.moviex.base.extensions.hideKeyboard
import com.akeshishi.moviex.base.extensions.makeGone
import com.akeshishi.moviex.base.extensions.makeVisible
import com.akeshishi.moviex.databinding.FragmentPopularGenresBinding
import com.akeshishi.moviex.features.movies.movielist.adapter.MovieListAdapter
import com.akeshishi.moviex.utils.MOVIE
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.IOException

/**
 * This fragment contains the movie or tv show list, according to the chosen genre in the [HomeFragment].
 * Access to this page is from the [HomeFragment].
 */
class PopularGenresFragment : Fragment() {

    private val args: PopularGenresFragmentArgs by navArgs()
    private val viewModel: PopularGenresViewModel by viewModel{ parametersOf(args.genreId)}
    private lateinit var viewBinding: FragmentPopularGenresBinding
    private lateinit var popularGenresListAdapter: MovieListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentPopularGenresBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.lifecycleOwner = viewLifecycleOwner
        viewBinding.toolbar.txtTitle.text = args.genreTitle
        setUpAdapter()
        loadData()
        setOnClickListener()
    }

    private fun setUpAdapter() {
        popularGenresListAdapter = MovieListAdapter(requireContext()) {
            val action = if (args.mediaType == MOVIE)
                PopularGenresFragmentDirections.popularGenresListFragmentToMovieDetailFragment(it)
            else
                PopularGenresFragmentDirections.popularGenresListFragmentToShowDetailFragment(it)
            findNavController().navigate(action)
        }
        viewBinding.rvPopularGenres.adapter = popularGenresListAdapter

        viewBinding.rvPopularGenres.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if ((recyclerView.layoutManager as LinearLayoutManager)
                            .findFirstVisibleItemPosition() == 0
                    ) viewBinding.fabUp.makeGone() else viewBinding.fabUp.makeVisible()
                }
            }
        )
    }

    private fun loadData() {
        lifecycleScope.launch {
            viewModel.getMovieList(args.mediaType)
            viewModel.celebrityDataFlow?.collectLatest { popularGenresListAdapter.submitData(it) }
        }
        handleLoading()
    }

    private fun handleLoading() {
        lifecycleScope.launch {
            popularGenresListAdapter.loadStateFlow.collectLatest {
                viewBinding.prgData.isVisible = it.refresh is LoadState.Loading
                when (it.refresh) {
                    is LoadState.Error -> {
                        viewBinding.prgData.makeGone()
                        when((it.refresh as LoadState.Error).error) {
                            is IOException -> viewBinding.networkErrorPage.networkErrorPage.makeVisible()
                            else -> getErrorMessage((it.refresh as LoadState.Error).error)
                        }
                    }
                }
            }
        }
    }

    private fun setOnClickListener() {
        viewBinding.toolbar.imgDrawer.setOnClickListener {
            requireView().hideKeyboard()
            findNavController().navigateUp()
        }
        viewBinding.fabUp.setOnClickListener {
            viewBinding.rvPopularGenres.smoothScrollToPosition(0)
        }
        viewBinding.networkErrorPage.txtTryAgainBtn.setOnClickListener {
            viewBinding.networkErrorPage.networkErrorPage.makeGone()
            popularGenresListAdapter.retry()
        }
    }
}