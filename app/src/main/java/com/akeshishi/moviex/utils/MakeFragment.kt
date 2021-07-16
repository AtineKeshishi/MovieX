package com.akeshishi.moviex.utils

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.akeshishi.moviex.features.celebrity.CelebrityCreditFragment
import com.akeshishi.moviex.features.menu.MoviesSectionFragment
import com.akeshishi.moviex.pojo.menu.MoviesSection

/**
 * Used to put value bundle as arguments into destination fragment.
 *
 * @param mediaType is the type of the media
 * @return fragment that save content type and value.
 */
fun makeFragment(mediaType: String): Fragment {
    val fragment = CelebrityCreditFragment()
    val bundle = Bundle()
    bundle.putString(TYPE, mediaType)
    fragment.arguments = bundle
    return fragment
}


fun makeBottomSheetMenu(selectedSection: MoviesSection, pageType: String): MoviesSectionFragment {
    return MoviesSectionFragment().apply {
        arguments = bundleOf(SELECTED_SECTION to selectedSection, TYPE to pageType)
    }
}