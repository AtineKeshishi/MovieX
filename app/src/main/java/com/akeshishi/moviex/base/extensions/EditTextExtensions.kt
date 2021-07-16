package com.akeshishi.moviex.base.extensions

import android.view.inputmethod.EditorInfo
import android.widget.EditText

/**
 * Soft keyboard for search and done click.
 *
 * @param searching The action of search button in keyboard.
 */
fun EditText.imeSearchClick(searching: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
            searching()
            true
        } else
            false
    }
}