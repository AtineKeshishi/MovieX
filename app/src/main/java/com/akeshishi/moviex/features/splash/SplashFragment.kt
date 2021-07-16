package com.akeshishi.moviex.features.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.akeshishi.moviex.databinding.FragmentSplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * This fragment is a loading page.
 */
class SplashFragment : Fragment() {

    lateinit var viewBinding : FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentSplashBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.lifecycleOwner = viewLifecycleOwner
        logInToHome()

    }

    private fun logInToHome() {
        lifecycleScope.launch {
            delay(3000)
            val action = SplashFragmentDirections.splashFragmentToHomeFragment(null)
            findNavController().navigate(action)
        }
    }
}