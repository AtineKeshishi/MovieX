package com.example.moviedb.popularMovies

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.Injection
import com.example.moviedb.paging.NetworkState
import com.example.moviedb.R
import com.example.moviedb.adapter.PopularMoviesAdapter
import com.example.moviedb.data.Movie

class PopularMovies : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recAdapter: PopularMoviesAdapter
    private lateinit var viewModel : PopularMoviesViewModel
    private lateinit var progressBar: ProgressBar
    private lateinit var errorPopular : TextView
    private lateinit var query : String
    var dataList: List<Movie> = ArrayList()
    var searchView : SearchView? = null

    companion object {
        private const val LAST_SEARCH_QUERY = "last_search_query"
        private const val DEFAULT_QUERY = "Home"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        this.setHasOptionsMenu(true)

        val view = inflater.inflate(R.layout.fragment_popular_movies, container, false)


        recyclerView = view.findViewById(R.id.recyclerView)
        progressBar = view.findViewById(R.id.progressBar)
        errorPopular = view.findViewById(R.id.errorPopular)

        viewModel = ViewModelProviders.of(this, Injection.provideViewModelFactory(context!!))
            .get(PopularMoviesViewModel::class.java)


        callAdapter()
        query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
        viewModel.searchMovie(query)


        return view
    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        outState.putString(LAST_SEARCH_QUERY, viewModel.lastQueryValue())
//    }

    private fun callAdapter(){
        val gridLayoutManager = GridLayoutManager(context, 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = recAdapter.getItemViewType(position)
                if (viewType == recAdapter.MOVIE_VIEW) return  1
                else return 2
            }
        }

        recAdapter = PopularMoviesAdapter()
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = recAdapter

        progressBar.visibility = View.VISIBLE

        viewModel.moviePagedList.observe(viewLifecycleOwner, Observer {
            recAdapter.submitList(it)
        })

        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            progressBar.visibility = if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            errorPopular.visibility = if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE
            if (!viewModel.listIsEmpty()) {
                recAdapter.setNetworkState(it)
            }
        })

    }

     private fun hideKeyboard() {
        if (searchView?.hasFocus() == true) searchView?.clearFocus()
    }

    private fun search (query : String) {
            query.trim().let {
            if (it.isNotEmpty()) {
                    recyclerView.scrollToPosition(0)
                    viewModel.searchMovie(it)
                    recAdapter.submitList(null)
                hideKeyboard()
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        searchView = menu.findItem(R.id.appBarSearch).actionView as SearchView
        searchView!!.queryHint = getString(R.string.queryHint)
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                search(query)
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.appBarSearch){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}

class ViewModelFactory(private val movieRepository : MoviePagedListRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PopularMoviesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PopularMoviesViewModel(movieRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

