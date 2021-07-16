package com.akeshishi.moviex.features.celebrity

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.akeshishi.moviex.R
import com.akeshishi.moviex.base.extensions.getErrorMessage
import com.akeshishi.moviex.base.extensions.makeGone
import com.akeshishi.moviex.base.extensions.makeVisible
import com.akeshishi.moviex.databinding.FragmentCelebrityCreditBinding
import com.akeshishi.moviex.features.celebrity.adapter.CelebrityCreditAdapter
import com.akeshishi.moviex.utils.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.getViewModel

/**
 * This fragment contains the celebrity's movie or tv show lists.
 */
class CelebrityCreditFragment : Fragment() {
    private val sharedPreferences: SharedPreferences by inject()
    private val viewModel: CelebrityDetailViewModel by lazy { requireParentFragment().getViewModel() }
    private lateinit var viewBinding: FragmentCelebrityCreditBinding
    private lateinit var actorCreditAdapter: CelebrityCreditAdapter
    private lateinit var mediaTypeId: String
    private lateinit var mediaType: String

    override fun onCreateView(
        inflater: LayoutInflater, parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentCelebrityCreditBinding.inflate(inflater)
        viewBinding.lifecycleOwner = viewLifecycleOwner
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setUpActorFilmAdapter()
        loadCreditData()
    }

    private fun init() {
        mediaTypeId = arguments?.getString(TYPE) ?: ""
        require(mediaTypeId.isNotEmpty()) { R.string.not_available }
        mediaType =
            if (mediaTypeId == sharedPreferences.getString(MOVIE_TYPE, DEFAULT)) MOVIE_CREDITS
            else TV_CREDITS
    }

    private fun setUpActorFilmAdapter() {
        actorCreditAdapter = CelebrityCreditAdapter(requireContext()) { id ->
            val action = if (mediaType == MOVIE_CREDITS)
                CelebrityDetailFragmentDirections.celebrityDetailFragmentToMovieDetailFragment(id)
            else
                CelebrityDetailFragmentDirections.celebrityDetailFragmentToShowDetailFragment(id)
            findNavController().navigate(action)
        }
        viewBinding.rvMedia.adapter = actorCreditAdapter

    }

    private fun loadCreditData() {
        lifecycleScope.launch {
            viewModel.getActorCreditList(mediaType)
                ?.collectLatest { actorCreditAdapter.submitData(it) }
        }
        handleLoading()
    }

    private fun handleLoading() {
        lifecycleScope.launch {
            actorCreditAdapter.loadStateFlow.collectLatest {
                viewBinding.prgMovieData.isVisible = it.refresh is LoadState.Loading
                when (it.refresh) {
                    is LoadState.NotLoading -> {
                        if (actorCreditAdapter.itemCount == 0) {
                            viewBinding.notFound.makeVisible()
                            viewBinding.notFound.text = getString(R.string.info_not_available)
                        }
                    }
                    is LoadState.Error -> {
                        viewBinding.prgMovieData.makeGone()
                        getErrorMessage((it.refresh as LoadState.Error).error)
                    }
                }
            }
        }
    }
}