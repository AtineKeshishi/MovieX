package com.akeshishi.moviex.features.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.akeshishi.moviex.R
import com.akeshishi.moviex.base.extensions.getErrorMessage
import com.akeshishi.moviex.base.extensions.hideKeyboard
import com.akeshishi.moviex.base.extensions.makeGone
import com.akeshishi.moviex.base.extensions.makeVisible
import com.akeshishi.moviex.databinding.FragmentSearchBinding
import com.akeshishi.moviex.features.search.adapter.SearchAdapter
import com.akeshishi.moviex.utils.MOVIE
import com.akeshishi.moviex.utils.TV
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.IOException

/**
 * This fragment contains the result list of the user's search.
 * Access to this page is from the [HomeFragment].
 */
class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModel()
    private lateinit var viewBinding: FragmentSearchBinding
    private lateinit var searchResultAdapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentSearchBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.lifecycleOwner = viewLifecycleOwner
        setUpAdapter()
        observeDefaultPage()
        loadData()
        setOnClickListener()
        checkTextViewFocus()
    }

    private fun setUpAdapter() {
        searchResultAdapter = SearchAdapter(requireContext()) { id, type ->
            requireView().hideKeyboard()
            val action = when (type) {
                MOVIE -> SearchFragmentDirections.searchFragmentToMovieDetailFragment(id)
                TV -> SearchFragmentDirections.searchFragmentToShowDetailFragment(id)
                else -> SearchFragmentDirections.searchFragmentToCelebrityDetailFragment(id)
            }
            findNavController().navigate(action)
        }
        viewBinding.searchMovieList.adapter = searchResultAdapter
    }

    private fun observeSearchData() {
        viewModel.search(viewModel.getSearchTerm())
        lifecycleScope.launch {
            viewModel.searchDataFlow?.collectLatest {
                searchResultAdapter.submitData(it)
            }
        }
        handleLoading()
    }

    private fun loadData() {
        if (viewModel.getSearchTerm().isNotEmpty())
            observeSearchData()
        else
            viewBinding.searchMovieList.makeVisible()
    }

    private fun setupSearch() {
        searchResult(viewBinding.edtSearchBox.text.toString())
        if (viewBinding.edtSearchBox.text.isEmpty())
            removeSearch()
    }

    private fun searchResult(word: String) {
        if (viewBinding.edtSearchBox.text.isNotEmpty()) {
            requireView().hideKeyboard()
            viewModel.saveSearchTerm(word)
            observeSearchData()
        }
    }

    private fun removeSearch() {
        viewBinding.edtSearchBox.text.clear()
        requireView().hideKeyboard()
        viewBinding.searchHeader.requestFocus()
        viewBinding.searchMovieList.makeGone()
        viewBinding.defaultEmptyPage.defaultPage.makeVisible()
        viewModel.saveSearchTerm("")
    }

    private fun showNoResultPage(itemCount: Int) {
        if (itemCount == 0) {
            viewBinding.searchMovieList.makeGone()
            viewBinding.defaultEmptyPage.defaultPage.makeGone()
            viewBinding.noResultPage.noResult.makeVisible()
        } else {
            viewBinding.searchMovieList.makeVisible()
            viewBinding.noResultPage.noResult.makeGone()
        }
    }

    private fun handleLoading() {
        lifecycleScope.launch {
            searchResultAdapter.loadStateFlow.collectLatest {
                handleResearch()
                viewBinding.prgData.isVisible = it.refresh is LoadState.Loading
                when (it.refresh) {
                    is LoadState.NotLoading -> {
                        viewBinding.networkErrorPage.networkErrorPage.makeGone()
                        showNoResultPage(searchResultAdapter.itemCount)
                    }
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

    private fun handleResearch() {
        viewBinding.searchMovieList.makeGone()
        viewBinding.defaultEmptyPage.defaultPage.makeGone()
        viewBinding.noResultPage.noResult.makeGone()
    }

    private fun observeDefaultPage() {
        if (viewModel.getSearchTerm().isNotEmpty()) {
            viewBinding.searchMovieList.makeVisible()
            viewBinding.defaultEmptyPage.defaultPage.makeGone()
        } else {
            viewBinding.searchMovieList.makeGone()
            viewBinding.defaultEmptyPage.defaultPage.makeVisible()
        }
    }

    private fun checkTextViewFocus() {
        viewBinding.edtSearchBox.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && viewModel.getSearchTerm().isNotEmpty()) {
                viewBinding.edtSearchBox.setHintTextColor(
                    ContextCompat.getColor(requireContext(), R.color.grayCloud)
                )
                viewBinding.imgClearSearchBox.makeVisible()
            } else {
                viewBinding.edtSearchBox.setHintTextColor(
                    ContextCompat.getColor(requireContext(), R.color.platinum)
                )
                viewBinding.imgClearSearchBox.makeGone()
            }

        }
    }

    private fun setOnClickListener() {
        viewBinding.imgBack.setOnClickListener {
            requireView().hideKeyboard()
            findNavController().navigateUp()
        }
        viewBinding.imgSearch.setOnClickListener { setupSearch() }
        viewBinding.edtSearchBox.setOnClickListener {
            viewBinding.edtSearchBox.clearFocus()
            viewBinding.edtSearchBox.requestFocus()
        }
        viewBinding.imgClearSearchBox.setOnClickListener { removeSearch() }
        viewBinding.networkErrorPage.txtTryAgainBtn.setOnClickListener {
            viewBinding.networkErrorPage.networkErrorPage.makeGone()
            searchResultAdapter.retry()
        }
    }
}
