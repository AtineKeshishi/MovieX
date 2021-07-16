package com.akeshishi.moviex.features.shows.showdetails.seasondetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akeshishi.moviex.pojo.shows.SeasonDetails
import com.akeshishi.moviex.repository.NetworkRepositoryInterface
import com.akeshishi.moviex.utils.GeneralResponse
import kotlinx.coroutines.launch

/**
 * ViewModel class of [SeasonDetailsFragment].
 * In this class, the services of the [SeasonDetailsFragment] are being called.
 * @see ShowRetrofitInterface
 *
 * @property tvId is the tv show's unique id.
 * @property repository is the repository to call API service.
 */

class SeasonDetailsViewModel(
    private val tvId: Int,
    private val seasonNumber: Int,
    private val repository: NetworkRepositoryInterface
) : ViewModel() {

    private val seasonDetailsLiveDate = MutableLiveData<GeneralResponse<SeasonDetails>>()

    fun getSeasonLiveDate(): LiveData<GeneralResponse<SeasonDetails>> = seasonDetailsLiveDate

    fun getSeasonDetailsData() {
        seasonDetailsLiveDate.value = GeneralResponse.Loading()
        viewModelScope.launch {
            try {
                val response = repository.getSeasonDetails(tvId, seasonNumber)
                seasonDetailsLiveDate.value = GeneralResponse.Success(response)
            } catch (exception: Exception){
                seasonDetailsLiveDate.value = GeneralResponse.Error(exception)
            }

        }
    }
}