package com.akeshishi.moviex.base.extensions

import androidx.fragment.app.Fragment
import com.akeshishi.moviex.R
import com.akeshishi.moviex.utils.NOT_FOUND_CODE
import com.akeshishi.moviex.utils.UNAUTHORIZED_CODE
import retrofit2.HttpException
import java.io.IOException
import java.io.InterruptedIOException
import java.net.UnknownHostException

/**
 * Exception handler for error's different status codes.
 */
fun Fragment.getErrorMessage(exception: Throwable?) {
    when (exception) {
        is InterruptedIOException, is UnknownHostException, is IOException ->
            requireView().snackBar(getString(R.string.check_internet))
        is HttpException -> when (exception.code()) {
            UNAUTHORIZED_CODE -> requireView().snackBar(getString(R.string.error_401))
            NOT_FOUND_CODE -> requireView().snackBar(getString(R.string.error_404))

        }
        else -> requireView().snackBar(getString(R.string.something_wrong))
    }
}
