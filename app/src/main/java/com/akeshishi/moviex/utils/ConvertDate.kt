package com.akeshishi.moviex.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

/**
 * This function is used to change the date format.
 */
@SuppressLint("SimpleDateFormat")
fun convertDate(date: String): String {
    val dateArray = date.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    val sdf = SimpleDateFormat("yyyy-MM-dd")
    val timeInMilliseconds = sdf.parse(date)?.time
    val dateString =
        SimpleDateFormat("MMMM dd", Locale.getDefault()).format(timeInMilliseconds)
            .split(" ".toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()
    return dateString[0].substring(0, 3) + " " + dateString[1] + ", " + dateArray[0]
}