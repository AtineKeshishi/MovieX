package com.akeshishi.moviex.base

import android.app.Application
import com.akeshishi.moviex.base.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * BaseApplication class to provide app level dependencies.
 */
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@BaseApplication)
            modules(
                moshiConverterFactoryModule,
                moshiModule,
                retrofitModule,
                repositoryModule,
                viewModelModule,
                sharedPreferencesModule
            )
        }
    }
}