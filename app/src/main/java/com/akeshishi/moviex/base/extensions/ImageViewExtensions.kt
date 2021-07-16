package com.akeshishi.moviex.base.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide

/**
 * Loads images into ImageView
 *
 * @param url The route url of image response.
 * @param placeholder The placeholder of image in view.
 */
fun ImageView.loadImage(url: String, placeholder: Int? = null) {
    val glide = Glide.with(context).load(url)
    placeholder?.let { glide.placeholder(it) }
    glide.into(this)
}