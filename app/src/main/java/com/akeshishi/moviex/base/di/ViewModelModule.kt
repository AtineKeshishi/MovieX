package com.akeshishi.moviex.base.di

import com.akeshishi.moviex.features.celebrity.CelebrityDetailViewModel
import com.akeshishi.moviex.features.home.HomeViewModel
import com.akeshishi.moviex.features.home.genres.PopularGenresViewModel
import com.akeshishi.moviex.features.movies.moviedetails.MovieDetailViewModel
import com.akeshishi.moviex.features.movies.movielist.MovieListViewModel
import com.akeshishi.moviex.features.movies.similar.SimilarMovieViewModel
import com.akeshishi.moviex.features.search.SearchViewModel
import com.akeshishi.moviex.features.shows.showdetails.ShowDetailsViewModel
import com.akeshishi.moviex.features.shows.showdetails.episodedetails.EpisodeDetailsViewModel
import com.akeshishi.moviex.features.shows.showdetails.seasondetails.SeasonDetailsViewModel
import com.akeshishi.moviex.features.shows.showlist.ShowListViewModel
import com.akeshishi.moviex.repository.DataRepository
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Represents instance of all ViewModels and injects [DataRepository] in ViewModels.
 */
val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { MovieListViewModel(get()) }
    viewModel { ShowListViewModel(get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { (genreId: Int) -> PopularGenresViewModel(genreId, get()) }
    viewModel { (movieId: Int) -> MovieDetailViewModel(movieId, get()) }
    viewModel { (showId: Int) -> ShowDetailsViewModel(showId, get()) }
    viewModel { (movieId: Int) -> SimilarMovieViewModel(movieId, get()) }
    viewModel { (personId: Int) -> CelebrityDetailViewModel(personId, get()) }
    viewModel { (tvId: Int, seasonNumber: Int) -> SeasonDetailsViewModel(tvId, seasonNumber, get()) }
    viewModel { (tvId: Int, seasonNumber: Int, episodeNumber: Int) ->
        EpisodeDetailsViewModel(tvId, seasonNumber, episodeNumber, get())
    }
}