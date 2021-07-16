package com.akeshishi.moviex.features.shows.showdetails.episodedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akeshishi.moviex.pojo.shows.EpisodeListResult
import com.akeshishi.moviex.repository.NetworkRepositoryInterface
import com.akeshishi.moviex.utils.GeneralResponse
import kotlinx.coroutines.launch

/**
 * ViewModel class of [EpisodeDetailsFragment].
 * In this class, the services of the [EpisodeDetailsFragment] are being called.
 * @see ShowRetrofitInterface
 *
 * @property tvId is the tv show's unique id.
 * @property repository is the repository to call API service.
 */
class EpisodeDetailsViewModel(
    private val tvId: Int,
    private val seasonNumber: Int,
    private val episodeNumber: Int,
    private val repository: NetworkRepositoryInterface
) : ViewModel() {
    private val episodeListLiveDate = MutableLiveData<GeneralResponse<EpisodeListResult>>()

    fun getEpisodeDetailLiveDate(): LiveData<GeneralResponse<EpisodeListResult>> =
        episodeListLiveDate

    fun getEpisodeDetailData() {
        episodeListLiveDate.value = GeneralResponse.Loading()
        viewModelScope.launch {
            try {
                val response = repository.getEpisodeDetails(tvId, seasonNumber, episodeNumber)
                episodeListLiveDate.value = GeneralResponse.Success(response)
            } catch (exception: Exception) {
                episodeListLiveDate.value = GeneralResponse.Error(exception)
            }
        }
    }
}