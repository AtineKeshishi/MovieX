package com.example.moviedb.favoriteMovies

import android.app.Application
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.R
import com.example.moviedb.adapter.FavoriteAdapter


class FavoriteMoviesList : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recAdapter: FavoriteAdapter
    private lateinit var viewModel : FavoriteMovieViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_favorite_movies, container, false)

        val application = requireNotNull(this.activity).application
        val viewModelFactory = ViewModelFactory(application)

        recyclerView = view.findViewById(R.id.recyclerView)

        recAdapter = FavoriteAdapter(context!!)
        recyclerView.layoutManager= LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        recyclerView.adapter = recAdapter

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(FavoriteMovieViewModel::class.java)

        viewModel.getAllMovies.observe(viewLifecycleOwner, Observer {movies ->
            movies.let { recAdapter.updateList(it) }
        })

        return view
    }
}

class ViewModelFactory (private val application: Application): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteMovieViewModel::class.java)) {
            return FavoriteMovieViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}
