package com.akeshishi.moviex.features.celebrity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.akeshishi.moviex.R
import com.akeshishi.moviex.base.extensions.getErrorMessage
import com.akeshishi.moviex.base.extensions.loadImage
import com.akeshishi.moviex.base.extensions.makeGone
import com.akeshishi.moviex.base.extensions.makeVisible
import com.akeshishi.moviex.databinding.FragmentCelebrityDetailsBinding
import com.akeshishi.moviex.features.celebrity.adapter.CelebrityPagerAdapter
import com.akeshishi.moviex.utils.GeneralResponse
import com.akeshishi.moviex.utils.POSTER_BASE_URL
import com.akeshishi.moviex.utils.convertDate
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.IOException
import java.util.*

/**
 * This fragment contains detailed information about celebrities.
 * Access to this page is from the [HomeFragment].
 */
class CelebrityDetailFragment : Fragment() {
    private val args: CelebrityDetailFragmentArgs by navArgs()
    private val viewModel: CelebrityDetailViewModel by viewModel { parametersOf(args.personId) }
    private lateinit var viewBinding: FragmentCelebrityDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentCelebrityDetailsBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.lifecycleOwner = viewLifecycleOwner
        loadActorData()
        setUpViewPager()
        setOnClickListener()
    }

    @SuppressLint("StringFormatMatches")
    private fun loadActorData() {
        viewModel.getActorDetail()
        viewModel.getActorLiveData().removeObservers(viewLifecycleOwner)
        viewModel.getActorLiveData().observe(viewLifecycleOwner) { response ->
            response.data?.let { actor ->
                viewBinding.txtName.text = actor.name
                viewBinding.txtBirthDay.text =
                    getString(
                        R.string.birthday, if (actor.birthday.isNullOrEmpty())
                            getString(R.string.not_available)
                        else convertDate(actor.birthday)
                            .plus("\t (${getString(R.string.age, calculateAge(actor.birthday))})"
                        )
                    )

                viewBinding.txtBirthPlace.text =
                    actor.placeOfBirth ?: viewBinding.txtBirthPlace.makeGone().toString()

                viewBinding.txtOccupation.text = actor.knownForDepartment

                if (!actor.deathday.isNullOrEmpty()) {
                    viewBinding.txtDeath.makeVisible()
                    viewBinding.txtDeath.text = getString(R.string.death, convertDate(actor.deathday))
                }

                viewBinding.imgPoster.loadImage(POSTER_BASE_URL + actor.profilePath,
                    R.drawable.person_placeholder
                )
            }

            when (response) {
                is GeneralResponse.Loading -> viewBinding.loadingPage.prgLoadingPage.makeVisible()
                is GeneralResponse.Success -> {
                    viewBinding.loadingPage.prgLoadingPage.makeGone()
                    viewBinding.networkErrorPage.networkErrorPage.makeGone()
                }
                is GeneralResponse.Error -> {
                    viewBinding.loadingPage.prgLoadingPage.makeGone()
                    when(response.cause){
                        is IOException -> viewBinding.networkErrorPage.networkErrorPage.makeVisible()
                        else -> getErrorMessage(response.cause)
                    }
                }
            }
        }
    }

    private fun setUpViewPager() {
        viewBinding.pager.isUserInputEnabled = false
        viewBinding.pager.adapter = CelebrityPagerAdapter(this)
        TabLayoutMediator(
            viewBinding.tabLayout,
            viewBinding.pager
        ) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.about)
                1 -> tab.text = getString(R.string.movies)
                2 -> tab.text = getString(R.string.shows)
                else -> throw IllegalArgumentException("Page title is not provided")
            }
        }.attach()
    }

    private fun calculateAge(date: String): Int {
        val currentYear: Int = Calendar.getInstance().get(Calendar.YEAR)
        val birthDay = date.substringBefore('-').toInt()
        return currentYear - birthDay
    }

    private fun setOnClickListener() {
        viewBinding.toolbar.imgDrawer.setOnClickListener { findNavController().navigateUp() }
        viewBinding.networkErrorPage.txtTryAgainBtn.setOnClickListener { loadActorData() }
    }
}

