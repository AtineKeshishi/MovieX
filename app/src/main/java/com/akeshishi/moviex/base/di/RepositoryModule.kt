package com.akeshishi.moviex.base.di

import com.akeshishi.moviex.repository.DataRepository
import com.akeshishi.moviex.repository.NetworkRepository
import com.akeshishi.moviex.repository.NetworkRepositoryInterface
import org.koin.dsl.module

/**
 * Represents module of koin that the scopes inside it creates instance of [DataRepository] &
 * [NetworkRepository].
 * This module injects [NetworkRepository] in [DataRepository].
 */
val repositoryModule = module {
    single<NetworkRepositoryInterface> {
        DataRepository(get())
    }

    single {
        NetworkRepository(get(), get(), get())
    }
}
