package com.akeshishi.moviex.features.celebrity.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.akeshishi.moviex.features.celebrity.CelebrityBiographyFragment
import com.akeshishi.moviex.utils.MOVIE
import com.akeshishi.moviex.utils.TV
import com.akeshishi.moviex.utils.makeFragment


/**
 * An adapter for [CelebrityDetailFragment] to generate 3 tabs.
 *
 * @param fragment is the fragment that is represented in different tabs.
 */
class CelebrityPagerAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = 3

    override fun createFragment(position: Int) = when (position) {
        0 -> CelebrityBiographyFragment()
        1 -> makeFragment(MOVIE)
        2 -> makeFragment(TV)
        else -> throw IllegalArgumentException("Fragment is not provided")
    }
}