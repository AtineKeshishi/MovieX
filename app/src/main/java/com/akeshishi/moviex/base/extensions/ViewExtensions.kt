package com.akeshishi.moviex.base.extensions

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.akeshishi.moviex.R
import com.google.android.material.snackbar.Snackbar

/**
 * Makes a view visible.
 */
fun View.makeVisible() {
    visibility = View.VISIBLE
}

/**
 * Makes a view gone.
 */
fun View.makeGone() {
    visibility = View.GONE
}

/**
 * Hide the keyboard
 */
fun View.hideKeyboard() {
    val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}

fun View.snackBar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
        .apply { view.elevation = 1000F }
        .setBackgroundTint(ContextCompat.getColor(context, R.color.colorGray))
        .show()
}