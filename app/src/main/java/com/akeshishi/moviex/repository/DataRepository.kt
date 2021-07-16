package com.akeshishi.moviex.repository

/**
 * All network repositories are stored here.
 *
 * @property networkRepository network observables.
 */
class DataRepository(private val networkRepository: NetworkRepository) :
        NetworkRepositoryInterface by networkRepository