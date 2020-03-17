package com.example.moviedb.singleMovie

import android.app.Application
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.moviedb.R
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moviedb.paging.NetworkState
import com.example.moviedb.api.POSTER_BASE_URL
import com.example.moviedb.data.MovieDetails
import com.example.moviedb.database.MovieDatabase
import com.example.moviedb.database.MovieEntity
import com.google.android.material.color.MaterialColors.getColor
import com.google.android.material.snackbar.Snackbar


import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_movie_detail.*
import kotlinx.android.synthetic.main.fragment_movie_detail.movieTitle
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

class SingleMovie : Fragment() {

    private lateinit var viewModel : SingleMovieViewModel
    private lateinit var progressBar: ProgressBar
    private lateinit var errorSingle : TextView
    private lateinit var database: MovieDatabase
    private lateinit var movieD : MovieDetails

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_movie_detail, container, false)
        this.setHasOptionsMenu(true)

        val movieId = SingleMovieArgs.fromBundle(arguments!!).transferMovieId
        val application = requireNotNull(this.activity).application
        val singleViewModelFactory = SingleViewModelFactory(movieId, application)
        val favButton = view.findViewById<ToggleButton>(R.id.buttonFavorite)


        database = MovieDatabase.getDatabase(context!!)

        progressBar = view.findViewById(R.id.progressBar)
        errorSingle = view.findViewById(R.id.errorSingle)


        viewModel = ViewModelProviders.of(this, singleViewModelFactory).get(SingleMovieViewModel::class.java)
        viewModel.movieDetails.observe(viewLifecycleOwner, Observer {
            bindUI(it)
        })

        viewModel.getMovieDetailsNetworkState().observe(viewLifecycleOwner, Observer {
            progressBar.visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            errorSingle.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })

        favButton.setOnCheckedChangeListener { buttonView, isChecked ->
            thread {
            if(isChecked){
                    val movieEnt = MovieEntity()
                    movieEnt.id = movieId
                    movieEnt.title = "${movieTitle.text}"
                    movieEnt.releaseDate = "${releaseDate.text}"
                    movieEnt.rating = "${movieRating.text}"
                    movieEnt.runtime = "${movieRuntime.text}"
                    movieEnt.budget = "${movieBudget.text}"
                    movieEnt.overview = "${movieOverview.text}"
                    movieEnt.posterPath = POSTER_BASE_URL + viewModel.movieDetails.value!!.posterPath

                    viewModel.insert(movieEnt)
                val snackbar : Snackbar = Snackbar.make(view, R.string.addToFavorites,Snackbar.LENGTH_SHORT)
                snackbar.show()
            } else {
                database.movieDao().delete(movieId)
                val snackbar : Snackbar = Snackbar.make(view, R.string.removeFromFavorites,Snackbar.LENGTH_SHORT)
                snackbar.show()
                  }
            }
        }

        return view
    }

    private fun bindUI(it: MovieDetails){
        movieTitle.text = it.title
        releaseDate.text = it.releaseDate
        movieRating.text = it.rating.toString()
        movieRuntime.text = it.runtime.toString() + " minutes"
        movieOverview.text = it.overview

        val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)
        movieBudget.text = formatCurrency.format(it.budget)

        val moviePosterURL = POSTER_BASE_URL + it.posterPath
        Picasso.get().load(moviePosterURL).into(poster)
    }

}

class SingleViewModelFactory (private val movieId:Int, private val application: Application): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SingleMovieViewModel::class.java)) {
            return SingleMovieViewModel(movieId, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}