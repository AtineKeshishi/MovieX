package com.akeshishi.moviex.features.movies.moviedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akeshishi.moviex.pojo.movies.CreditResult
import com.akeshishi.moviex.pojo.movies.MovieDetails
import com.akeshishi.moviex.pojo.movies.VideoResult
import com.akeshishi.moviex.repository.NetworkRepositoryInterface
import com.akeshishi.moviex.utils.GeneralResponse
import kotlinx.coroutines.launch

/**
 * ViewModel class of [MovieDetailFragment].
 * In this class, the services of the [MovieDetailFragment] are being called.
 * @see MovieRetrofitInterface
 *
 * @property movieId is the movie's unique id.
 * @property repository is the repository to call API service.
 */
class MovieDetailViewModel(
    private val movieId: Int,
    private val repository: NetworkRepositoryInterface
) : ViewModel() {

    private val movieDetailLiveDate = MutableLiveData<GeneralResponse<MovieDetails>>()
    private val castListLiveDate = MutableLiveData<GeneralResponse<CreditResult>>()
    private val videoListLiveDate = MutableLiveData<GeneralResponse<VideoResult>>()

    fun getMovieDetailLiveDate(): LiveData<GeneralResponse<MovieDetails>> = movieDetailLiveDate
    fun getCastListLiveData(): LiveData<GeneralResponse<CreditResult>> = castListLiveDate
    fun getVideoListLiveData(): LiveData<GeneralResponse<VideoResult>> = videoListLiveDate

    fun getMovieDetailData() {
        movieDetailLiveDate.value = GeneralResponse.Loading()
        viewModelScope.launch {
            try {
                val response = repository.getMovieDetails(movieId)
                movieDetailLiveDate.value = GeneralResponse.Success(response)
            } catch (exception: Exception) {
                movieDetailLiveDate.value = GeneralResponse.Error(exception)
            }
        }
    }

    fun getCastList() {
        castListLiveDate.value = GeneralResponse.Loading()
        viewModelScope.launch {
            try {
                val response = repository.getMovieCredits(movieId)
                castListLiveDate.postValue(GeneralResponse.Success(response))
            } catch (exception: Exception) {
                castListLiveDate.value = GeneralResponse.Error(exception)
            }
        }
    }

    fun getVideoList() {
        videoListLiveDate.value = GeneralResponse.Loading()
        viewModelScope.launch {
            try {
                val response = repository.getMovieVideos(movieId)
                videoListLiveDate.value = GeneralResponse.Success(response)
            } catch (exception: Exception) {
                videoListLiveDate.value = GeneralResponse.Error(exception)
            }
        }
    }
}