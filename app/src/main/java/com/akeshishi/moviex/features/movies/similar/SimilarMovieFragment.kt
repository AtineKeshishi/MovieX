package com.akeshishi.moviex.features.movies.similar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.akeshishi.moviex.R
import com.akeshishi.moviex.base.extensions.getErrorMessage
import com.akeshishi.moviex.base.extensions.hideKeyboard
import com.akeshishi.moviex.base.extensions.makeGone
import com.akeshishi.moviex.base.extensions.makeVisible
import com.akeshishi.moviex.databinding.FragmentSimilarMovieBinding
import com.akeshishi.moviex.features.movies.similar.adapter.SimilarMovieListAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
 * This fragment contains a list of similar movies.
 * Access to this page is from the [MovieDetailFragment].
 */
class SimilarMovieFragment : BottomSheetDialogFragment() {

    private val args: SimilarMovieFragmentArgs by navArgs()
    private val viewModel: SimilarMovieViewModel by viewModel { parametersOf(args.movieId) }
    private lateinit var viewBinding: FragmentSimilarMovieBinding
    private lateinit var similarMovieAdapter: SimilarMovieListAdapter


    override fun onCreateView(
        layoutInflater: LayoutInflater,
        parent: ViewGroup?,
        saved: Bundle?
    ): View {
        viewBinding = FragmentSimilarMovieBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.lifecycleOwner = viewLifecycleOwner
        setUpAdapter()
        loadData()
        backPressed()
    }

    override fun getTheme(): Int = R.style.CustomBottomSheetDialog

    private fun setUpAdapter() {
        similarMovieAdapter = SimilarMovieListAdapter(requireContext()) {
            val action =
                SimilarMovieFragmentDirections.similarMovieFragmentToMovieDetailFragment(it)
            findNavController().navigate(action)
        }
        viewBinding.rvSimilarMovieList.adapter = similarMovieAdapter
    }

    private fun loadData() {
        viewModel.getSimilarMovieList()
        lifecycleScope.launch {
            viewModel.similarDataFlow?.collectLatest {
                similarMovieAdapter.submitData(it)
            }
        }
        handleLoading()
    }

    private fun handleLoading() {
        lifecycleScope.launch {
            similarMovieAdapter.loadStateFlow.collectLatest {
                viewBinding.prgData.isVisible = it.refresh is LoadState.Loading
                when (it.refresh) {
                    is LoadState.NotLoading -> showNoSimilarMovies()
                    is LoadState.Error -> {
                        viewBinding.prgData.makeGone()
                        viewBinding.notFound.makeVisible()
                        viewBinding.notFound.text = getString(R.string.oops)
                        getErrorMessage((it.refresh as LoadState.Error).error)
                    }
                }
            }
        }
    }

    private fun showNoSimilarMovies() {
        if (similarMovieAdapter.itemCount == 0) {
            viewBinding.notFound.makeVisible()
            viewBinding.notFound.text = getString(R.string.no_similar_movie)
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