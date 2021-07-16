package com.akeshishi.moviex.pojo.menu

/**
 * The model to get the menu item details' result.
 */
data class MenuItemData(val moviesSection: MoviesSection, val title: String, val icon: Int)

/**
 * Provides the type of movie or show collection for fetching the data.
 *
 */
enum class MoviesSection {
    POPULAR,
    NOW_PLAYING,
    TOP_RATED,
    UPCOMING
}
