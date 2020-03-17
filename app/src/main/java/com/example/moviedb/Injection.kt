/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.moviedb

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.moviedb.api.ApiClient
import com.example.moviedb.popularMovies.MoviePagedListRepository
import com.example.moviedb.popularMovies.ViewModelFactory
import java.util.concurrent.Executors


object Injection {

    // thread pool used for network requests
    @Suppress("PrivatePropertyName")
    private val NETWORK_IO = Executors.newFixedThreadPool(10)

     //Creates an instance of MoviePagedListRepository based on the ApiClient.
    private fun provideRepository(context: Context): MoviePagedListRepository {
        return MoviePagedListRepository(ApiClient.getClient(), NETWORK_IO)
    }


//    Provides the ViewModelProvider.Factory that is then used to get a reference to ViewModel objects.
    fun provideViewModelFactory(context: Context): ViewModelProvider.Factory {
        return ViewModelFactory(provideRepository(context))
    }
}
