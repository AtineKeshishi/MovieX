package com.akeshishi.moviex.repository

import com.akeshishi.moviex.retrofit.CelebrityRetrofitInterface
import com.akeshishi.moviex.retrofit.MovieRetrofitInterface
import com.akeshishi.moviex.retrofit.ShowRetrofitInterface

/**
 * The Interface of the retrofit API call.
 */
interface NetworkRepositoryInterface :
    MovieRetrofitInterface,
    ShowRetrofitInterface,
    CelebrityRetrofitInterface