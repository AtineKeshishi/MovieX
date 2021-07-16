package com.akeshishi.moviex.features.celebrity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.akeshishi.moviex.R
import com.akeshishi.moviex.base.extensions.getErrorMessage
import com.akeshishi.moviex.base.extensions.makeVisible
import com.akeshishi.moviex.base.extensions.snackBar
import com.akeshishi.moviex.databinding.FragmentCelebrityBiographyBinding
import com.akeshishi.moviex.features.celebrity.adapter.CelebrityImageAdapter
import com.akeshishi.moviex.pojo.celebrity.Profile
import com.akeshishi.moviex.utils.GeneralResponse
import org.koin.android.viewmodel.ext.android.getViewModel

/**
 * This fragment contains the celebrity's biography.
 */
class CelebrityBiographyFragment : Fragment() {
    private val viewModel: CelebrityDetailViewModel by lazy { requireParentFragment().getViewModel() }
    private lateinit var viewBinding: FragmentCelebrityBiographyBinding
    var celebrityName = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentCelebrityBiographyBinding.inflate(inflater)
        viewBinding.lifecycleOwner = viewLifecycleOwner
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadActorData()
        loadImagesList()
        loadExternalId()
    }

    private fun loadActorData() {
        viewModel.getActorDetail()
        viewModel.getActorLiveData().removeObservers(viewLifecycleOwner)
        viewModel.getActorLiveData().observe(viewLifecycleOwner) { response ->
            response.data?.let { actor ->
                celebrityName = actor.name

                if (!actor.homepage.isNullOrEmpty()) {
                    viewBinding.homePageSection.makeVisible()
                    viewBinding.txtHomepage.text = actor.homepage
                }

                viewBinding.txtBiography.text =
                    if (actor.biography!!.isEmpty())
                        getString(R.string.info_not_available)
                    else actor.biography
            }

            when (response) {
                is GeneralResponse.Error -> getErrorMessage(response.cause)
            }
        }

    }

    private fun loadImagesList() {
        viewModel.getActorImageList()
        viewModel.getImageLiveData().removeObservers(viewLifecycleOwner)
        viewModel.getImageLiveData().observe(viewLifecycleOwner) {
            when (it) {
                is GeneralResponse.Success -> setUpActorImageAdapter(it.data!!.profiles)
            }
        }
    }

    private fun setUpActorImageAdapter(imageList: List<Profile>) {
        val actorImageAdapter = CelebrityImageAdapter(imageList)
        {
            val viewIntent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(getString(R.string.image_link, it))
            }
            startActivity(viewIntent)
        }
        viewBinding.rvImages.adapter = actorImageAdapter
    }

    private fun loadExternalId() {
        viewModel.getActorExternalId()
        viewModel.getExternalLiveData().removeObservers(viewLifecycleOwner)
        viewModel.getExternalLiveData().observe(viewLifecycleOwner) { response ->
            when (response) {
                is GeneralResponse.Success -> {
                    viewBinding.imgImdb.setOnClickListener {
                        if (!response.data?.imdbId.isNullOrEmpty())
                            social(R.string.imdb_link, response.data?.imdbId!!)
                        else requireView().snackBar(getString(R.string.no_account, celebrityName))
                    }
                    viewBinding.imgFacebook.setOnClickListener {
                        if (!response.data?.facebookId.isNullOrEmpty())
                            social(R.string.facebook_link, response.data?.facebookId!!)
                        else requireView().snackBar(getString(R.string.no_account, celebrityName))
                    }
                    viewBinding.imgInstagram.setOnClickListener {
                        if (!response.data?.instagramId.isNullOrEmpty())
                            social(R.string.instagram_link, response.data?.instagramId!!)
                        else requireView().snackBar(getString(R.string.no_account, celebrityName))
                    }
                    viewBinding.imgTwitter.setOnClickListener {
                        if (!response.data?.twitterId.isNullOrEmpty())
                            social(R.string.twitter_link, response.data?.twitterId!!)
                        else requireView().snackBar(getString(R.string.no_account, celebrityName))
                    }
                }
                is GeneralResponse.Error -> getErrorMessage(response.cause)
            }
        }
    }

    private fun social(link: Int, personInfo: String) {
        val viewIntent = Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(getString(link, personInfo))
        }
        startActivity(viewIntent)
    }
}