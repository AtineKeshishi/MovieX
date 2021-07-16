package com.akeshishi.moviex.features.celebrity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.akeshishi.moviex.features.celebrity.adapter.CelebrityCreditPageSource
import com.akeshishi.moviex.pojo.celebrity.ActorImageResult
import com.akeshishi.moviex.pojo.celebrity.CelebrityDetails
import com.akeshishi.moviex.pojo.celebrity.ExternalLinks
import com.akeshishi.moviex.pojo.search.CombinedMovies
import com.akeshishi.moviex.repository.NetworkRepositoryInterface
import com.akeshishi.moviex.utils.GeneralResponse
import com.akeshishi.moviex.utils.LIST_SIZE
import com.akeshishi.moviex.utils.MOVIE_CREDITS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * ViewModel class of [CelebrityDetailFragment].
 * In this class, the services of the [CelebrityDetailFragment] are being called.
 * @see com.akeshishi.moviex.retrofit.CelebrityRetrofitInterface
 *
 * @property personId is the celebrity's unique id.
 * @property repository is the repository to call API service.
 */
class CelebrityDetailViewModel(
    private val personId: Int,
    private val repository: NetworkRepositoryInterface
) : ViewModel() {

    private val actorLiveData = MutableLiveData<GeneralResponse<CelebrityDetails>>()
    private val imageLiveData = MutableLiveData<GeneralResponse<ActorImageResult>>()
    private val externalLiveData = MutableLiveData<GeneralResponse<ExternalLinks>>()

    private var creditMovieDataFlow: Flow<PagingData<CombinedMovies>>? = null
    private var creditTvDataFlow: Flow<PagingData<CombinedMovies>>? = null

    fun getActorLiveData(): LiveData<GeneralResponse<CelebrityDetails>> = actorLiveData
    fun getImageLiveData(): LiveData<GeneralResponse<ActorImageResult>> = imageLiveData
    fun getExternalLiveData(): LiveData<GeneralResponse<ExternalLinks>> = externalLiveData

    fun getActorDetail() {
        actorLiveData.value = GeneralResponse.Loading()
        viewModelScope.launch {
            try {
                val response = repository.celebrityDetails(personId)
                actorLiveData.value = GeneralResponse.Success(response)
            } catch (exception: Exception) {
                actorLiveData.value = GeneralResponse.Error(exception)
            }
        }
    }

    fun getActorCreditList(type: String): Flow<PagingData<CombinedMovies>>? {
        val lastMovieResult = creditMovieDataFlow
        val lastTvResult = creditTvDataFlow
        if (type == MOVIE_CREDITS) {
            if (lastMovieResult != null) return lastMovieResult
        } else {
            if (lastTvResult != null) return lastTvResult
        }
        val newResult = Pager(PagingConfig(pageSize = LIST_SIZE)) {
            CelebrityCreditPageSource(type, personId, repository)
        }.flow.cachedIn(viewModelScope)
        return if (type == MOVIE_CREDITS) {
            creditMovieDataFlow = newResult
            creditMovieDataFlow
        } else {
            creditTvDataFlow = newResult
            creditTvDataFlow
        }
    }

    fun getActorImageList() {
        imageLiveData.value = GeneralResponse.Loading()
        viewModelScope.launch {
            try {
                val response = repository.getCelebrityImages(personId)
                imageLiveData.value = GeneralResponse.Success(response)
            } catch (exception: Exception) {
                imageLiveData.value = GeneralResponse.Error(exception)
            }
        }
    }

    fun getActorExternalId() {
        externalLiveData.value = GeneralResponse.Loading()
        viewModelScope.launch {
            try {
                val response = repository.getExternalID(personId)
                externalLiveData.value = GeneralResponse.Success(response)
            } catch (exception: Exception) {
                externalLiveData.value = GeneralResponse.Error(exception)
            }
        }
    }
}