package com.akeshishi.moviex.features.shows.showlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akeshishi.moviex.R
import com.akeshishi.moviex.base.extensions.getErrorMessage
import com.akeshishi.moviex.base.extensions.hideKeyboard
import com.akeshishi.moviex.base.extensions.makeGone
import com.akeshishi.moviex.base.extensions.makeVisible
import com.akeshishi.moviex.databinding.FragmentShowListBinding
import com.akeshishi.moviex.features.menu.MoviesSectionFragment
import com.akeshishi.moviex.features.movies.movielist.adapter.MovieListAdapter
import com.akeshishi.moviex.pojo.menu.MoviesSection
import com.akeshishi.moviex.utils.SHOWS
import com.akeshishi.moviex.utils.TAG
import com.akeshishi.moviex.utils.makeBottomSheetMenu
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.IOException

/**
 * This fragment contains the list of tv shows.
 * Access to this page is from the [HomeFragment].
 */
class ShowListFragment : Fragment(), MoviesSectionFragment.Companion.NavigationInteractions {

    private val viewModel: ShowListViewModel by viewModel()
    private lateinit var viewBinding: FragmentShowListBinding
    private lateinit var showListAdapter: MovieListAdapter
    private lateinit var selectedSection: MoviesSection

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentShowListBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.lifecycleOwner = viewLifecycleOwner
        getSafeArgs()
        setToolbar()
        setUpAdapter()
        loadData()
        backPressed()
        setupBottomAppBar()
        setOnClickListener()
    }

    override fun onNavigationItemPicked(moviesSection: MoviesSection) {
        selectedSection = moviesSection
        setToolbar()
        val action = ShowListFragmentDirections.actionShowListFragmentSelf(selectedSection)

        findNavController().navigate(action)
    }

    private fun getSafeArgs() {
        val safeArgs: ShowListFragmentArgs by navArgs()
        selectedSection = safeArgs.selectedSection
    }

    private fun setToolbar() =
        viewBinding.txtToolbarTitle.setText(
            when (selectedSection) {
                MoviesSection.POPULAR -> R.string.popular
                MoviesSection.NOW_PLAYING -> R.string.on_the_air
                else -> R.string.not_available
            }
        )

    private fun setUpAdapter() {
        showListAdapter = MovieListAdapter(requireContext()) {
            val action = ShowListFragmentDirections.showListFragmentToShowDetailFragment(it)
            findNavController().navigate(action)
        }
        viewBinding.showList.adapter = showListAdapter

        viewBinding.showList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if ((recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition() == 0)
                    viewBinding.fabUp.makeGone() else viewBinding.fabUp.makeVisible()
            }
        })
    }

    private fun loadData() {
        lifecycleScope.launch {
            viewModel.getShowList(selectedSection)
            viewModel.showDataFlow?.collectLatest { showListAdapter.submitData(it) }
        }
        handleLoading()
    }

    private fun handleLoading() {
        lifecycleScope.launch {
            showListAdapter.loadStateFlow.collectLatest {
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

    private fun showBottomNavigationOptions() {
        makeBottomSheetMenu(selectedSection, SHOWS).show(childFragmentManager, TAG)
    }

    private fun setupBottomAppBar() = viewBinding.bottomAppBar.run {
        setNavigationOnClickListener { showBottomNavigationOptions() }
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
        viewBinding.fabUp.setOnClickListener { viewBinding.showList.smoothScrollToPosition(0) }
        viewBinding.networkErrorPage.txtTryAgainBtn.setOnClickListener {
            viewBinding.networkErrorPage.networkErrorPage.makeGone()
            showListAdapter.retry()
        }
    }
}