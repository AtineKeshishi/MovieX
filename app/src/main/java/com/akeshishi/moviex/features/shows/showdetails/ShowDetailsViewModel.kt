package com.akeshishi.moviex.features.shows.showdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akeshishi.moviex.pojo.movies.CreditResult
import com.akeshishi.moviex.pojo.shows.ShowDetails
import com.akeshishi.moviex.repository.NetworkRepositoryInterface
import com.akeshishi.moviex.utils.GeneralResponse
import kotlinx.coroutines.launch

/**
 * ViewModel class for [ShowDetailsFragment].
 * In this class, the services of the [ShowDetailsFragment] are being called.
 * @see ShowRetrofitInterface
 *
 * @property showId is the tv show's unique id.
 * @property repository is the repository to call API service.
 */
class ShowDetailsViewModel(
    private val showId: Int,
    private val repository: NetworkRepositoryInterface
) : ViewModel() {

    private val showDetailsLiveDate = MutableLiveData<GeneralResponse<ShowDetails>>()
    private val castListLiveDate = MutableLiveData<GeneralResponse<CreditResult>>()

    fun getShowDetailsLiveData(): LiveData<GeneralResponse<ShowDetails>> = showDetailsLiveDate
    fun getCastListLiveData(): LiveData<GeneralResponse<CreditResult>> = castListLiveDate

    fun getShowDetailData() {
        showDetailsLiveDate.value = GeneralResponse.Loading()
        viewModelScope.launch {
            try {
                val response = repository.getShowDetails(showId)
                showDetailsLiveDate.value = GeneralResponse.Success(response)
            } catch (exception: Exception) {
                showDetailsLiveDate.value = GeneralResponse.Error(exception)
            }
        }
    }

    fun getCastList() {
        castListLiveDate.value = GeneralResponse.Loading()
        viewModelScope.launch {
            try {
                val response = repository.getShowCredits(showId)
                castListLiveDate.value = GeneralResponse.Success(response)
            }catch (exception: Exception) {
                castListLiveDate.value = GeneralResponse.Error(exception)
            }
        }
    }
}