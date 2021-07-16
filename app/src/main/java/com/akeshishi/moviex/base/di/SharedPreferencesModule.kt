package com.akeshishi.moviex.base.di

import android.content.Context
import com.akeshishi.moviex.R
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val sharedPreferencesModule = module {

    single {
        /**
         * Provides sharedPreferences.
         */
        androidContext().getSharedPreferences(
            androidContext().getString(R.string.app_name),
            Context.MODE_PRIVATE
        )
    }
}