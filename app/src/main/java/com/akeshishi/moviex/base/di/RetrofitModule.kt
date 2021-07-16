package com.akeshishi.moviex.base.di


import com.akeshishi.moviex.BuildConfig.API_KEY
import com.akeshishi.moviex.BuildConfig.BASE_URL
import com.akeshishi.moviex.retrofit.MovieRetrofitInterface
import com.akeshishi.moviex.retrofit.CelebrityRetrofitInterface
import com.akeshishi.moviex.retrofit.ShowRetrofitInterface
import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

val retrofitModule = module {

    single {
        /**
         * Provides and configs logger to see the logs in terminal.
         */
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        interceptor
    }

    single {
        /**
         * Provides base url.
         */
        BASE_URL
    }

    single {
        /**
         * Provides and configs okHttp to have authorization header.
         */
        val httpClient = OkHttpClient().newBuilder()
            .addInterceptor(addKey())
            .addInterceptor(get<HttpLoggingInterceptor>())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .callTimeout(30, TimeUnit.SECONDS)
        httpClient

    }

    single {
        Retrofit.Builder()
            .client(get<OkHttpClient.Builder>().build())
            .baseUrl(get<String>())
            .addConverterFactory(get<MoshiConverterFactory>())
            .build()
    }

    /**
     * Provides the retrofit Movie interface.
     */
    single {
        get<Retrofit>().create(MovieRetrofitInterface::class.java)
    }

    /**
     * Provides the retrofit Show interface.
     */
    single {
        get<Retrofit>().create(ShowRetrofitInterface::class.java)
    }

    /**
     * Provides the retrofit Celebrity interface.
     */
    single {
        get<Retrofit>().create(CelebrityRetrofitInterface::class.java)
    }
}

/**
 * Represents instance of MoshiConverterFactory.
 */
val moshiConverterFactoryModule = module {
    single {
        /**
         * Provides MoshiConverterFactory.
         */
        MoshiConverterFactory.create(get())
    }
}

/**
 * Represents builder of Moshi.
 */
val moshiModule = module {
    single {
        /**
         * Provides MoshiConverterFactory.
         */
        Moshi.Builder().build()
    }
}

/**
 * Provides the API key.
 */
private fun addKey(): Interceptor {
    return Interceptor { chain ->
        var request: Request = chain.request()
        val url = request.url.newBuilder()
            .addQueryParameter("api_key", API_KEY)
            .build()

        request = request.newBuilder()
            .url(url)
            .build()
        chain.proceed(request)
    }
}
