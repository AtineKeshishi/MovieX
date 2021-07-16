package com.akeshishi.moviex.features.menu

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.akeshishi.moviex.R
import com.akeshishi.moviex.databinding.ItemBottomMenuBinding
import com.akeshishi.moviex.databinding.MenuBottomAppBarBinding
import com.akeshishi.moviex.pojo.menu.MenuItemData
import com.akeshishi.moviex.pojo.menu.MoviesSection
import com.akeshishi.moviex.pojo.menu.MoviesSection.*
import com.akeshishi.moviex.utils.MOVIES
import com.akeshishi.moviex.utils.SELECTED_SECTION
import com.akeshishi.moviex.utils.TYPE
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * Shows the bottom sheet to select Movie Section.
 */
class MoviesSectionFragment : BottomSheetDialogFragment() {

    private lateinit var interactions: NavigationInteractions
    private lateinit var viewBinding: MenuBottomAppBarBinding
    private lateinit var navViewBinding: ItemBottomMenuBinding
    private lateinit var selectedSection: MoviesSection
    private lateinit var pageType: String

    override fun onAttach(context: Context) {
        super.onAttach(context)
        interactions = when {
            context is NavigationInteractions -> context
            parentFragment is NavigationInteractions -> parentFragment as NavigationInteractions
            else -> throw IllegalStateException("Parent activity or fragment must implement NavigationInteractions")
        }
    }

    override fun onCreateView(
        layoutInflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = MenuBottomAppBarBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.lifecycleOwner = viewLifecycleOwner
        getSafeArgs()
        addNavigationItems()
    }

    private fun addNavigationItems() = viewBinding.navigationBottomSheet.run {
        removeAllViews()
        getNavigationItems().forEach { addView(buildBottomNavigationView(it)) }
    }

    private fun getSafeArgs() {
        pageType = arguments?.getString(TYPE).toString()
        selectedSection = arguments?.getSerializable(SELECTED_SECTION) as MoviesSection
    }

    private fun buildBottomNavigationView(itemData: MenuItemData) =
        layoutInflater.inflate(R.layout.item_bottom_menu, null).apply {
            navViewBinding = ItemBottomMenuBinding.bind(this)
            navViewBinding.imgIcon.setImageResource(itemData.icon)
            navViewBinding.txtTitle.text = itemData.title


            val isSelected = itemData.moviesSection == selectedSection
            if (isSelected) {
                navViewBinding.imgIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorSecondary))
                navViewBinding.txtTitle.setTextColor(resources.getColor(R.color.colorSecondaryLight, null))
            } else setOnClickListener {
                dismiss()
                interactions.onNavigationItemPicked(itemData.moviesSection)
            }
        }

    private fun getNavigationItems() = if (pageType == MOVIES) {
        listOf(
            MenuItemData(
                POPULAR,
                getString(R.string.popular),
                R.drawable.ic_popular
            ),
            MenuItemData(
                TOP_RATED,
                getString(R.string.top_rated),
                R.drawable.ic_top_rated
            ),
            MenuItemData(
                NOW_PLAYING,
                getString(R.string.now_playing),
                R.drawable.ic_now_playing
            ),
            MenuItemData(
                UPCOMING,
                getString(R.string.upcoming),
                R.drawable.ic_upcoming
            )
        )
    } else {
        listOf(
            MenuItemData(
                POPULAR,
                getString(R.string.popular),
                R.drawable.ic_popular
            ),
            MenuItemData(
                NOW_PLAYING,
                getString(R.string.on_the_air),
                R.drawable.ic_tv
            )
        )
    }

    companion object {
        interface NavigationInteractions {
            fun onNavigationItemPicked(moviesSection: MoviesSection)
        }
    }
}


