package com.akeshishi.moviex.base

/**
 * When an exception occurred, the response is wrapped by this model.
 * The response status can be [UnAuthorizedException] or [NotFoundException].
 *
 * @property message is the message of Error response.
 */
sealed class ExceptionResource(val message: String){
    class IOException(message: String) : ExceptionResource(message)
    class UnAuthorizedException(message: String) : ExceptionResource(message)
    class NotFoundException(message: String) : ExceptionResource(message)
}
