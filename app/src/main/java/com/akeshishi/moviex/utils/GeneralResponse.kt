package com.akeshishi.moviex.utils


/**
 * When a service gets called, the response is wrapped by this model.
 * The response status can be [Success] or [Loading].
 *
 * @param T is the type of response model, and it's generic.
 * @property data is the value of response from [T] type.
 */
sealed class GeneralResponse<T>(val data: T? = null, val cause: Throwable? = null) {

    /**
     * When we get the response successfully.
     */
    class Success<T>(data: T) : GeneralResponse<T>(data)

    /**
     * When we want to show the loading indicator.
     */
    class Loading<T>(data: T? = null) : GeneralResponse<T>(data)

    /**
     * When we have error.
     *
     * @param cause is the exception cause.
     */
    class Error<T>(cause: Throwable?) : GeneralResponse<T>(cause = cause)
}