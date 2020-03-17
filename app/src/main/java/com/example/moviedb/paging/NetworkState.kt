package com.example.moviedb.paging


enum class State {
    DONE, LOADING, ERROR
}


class NetworkState (val status: State, val msg: String){

companion object{
    val LOADED: NetworkState
    val LOADING: NetworkState
    val ERROR: NetworkState
    val ENDOFLIST: NetworkState

    init {
        LOADED = NetworkState(State.DONE, "Success")
        LOADING = NetworkState(State.LOADING, "Loading")
        ERROR = NetworkState(State.ERROR, "Something went wrong")
        ENDOFLIST = NetworkState(State.ERROR, "You have reached the end")
    }
}


}