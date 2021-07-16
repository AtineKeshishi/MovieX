package com.akeshishi.moviex

import android.net.ConnectivityManager
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import com.akeshishi.moviex.databinding.ActivityMainBinding
import com.akeshishi.moviex.features.home.HomeFragmentDirections
import com.akeshishi.moviex.pojo.menu.MoviesSection.*
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {

    lateinit var viewBinding: ActivityMainBinding
    var connectivity: ConnectivityManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        viewBinding.drawer.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.popularMovies -> {
                viewBinding.drawerLayout.closeDrawer(GravityCompat.START)
                findNavController(R.id.mainNavController).navigate(
                    HomeFragmentDirections.homeFragmentToMovieListFragment(POPULAR)
                )
            }
            R.id.topRatedMovies -> {
                viewBinding.drawerLayout.closeDrawer(GravityCompat.START)
                findNavController(R.id.mainNavController).navigate(
                    HomeFragmentDirections.homeFragmentToMovieListFragment(TOP_RATED)
                )
            }
            R.id.nowPlayingMovies -> {
                viewBinding.drawerLayout.closeDrawer(GravityCompat.START)
                findNavController(R.id.mainNavController).navigate(
                    HomeFragmentDirections.homeFragmentToMovieListFragment(NOW_PLAYING)
                )
            }
            R.id.upcomingMovies -> {
                viewBinding.drawerLayout.closeDrawer(GravityCompat.START)
                findNavController(R.id.mainNavController).navigate(
                    HomeFragmentDirections.homeFragmentToMovieListFragment(UPCOMING)
                )
            }
            R.id.popularShows -> {
                viewBinding.drawerLayout.closeDrawer(GravityCompat.START)
                findNavController(R.id.mainNavController).navigate(
                    HomeFragmentDirections.homeFragmentToShowListFragment(POPULAR)
                )
            }
            R.id.onTheAirShows -> {
                viewBinding.drawerLayout.closeDrawer(GravityCompat.START)
                findNavController(R.id.mainNavController).navigate(
                    HomeFragmentDirections.homeFragmentToShowListFragment(NOW_PLAYING)
                )
            }

            R.id.search -> {
                viewBinding.drawerLayout.closeDrawer(GravityCompat.START)
                findNavController(R.id.mainNavController).navigate(
                    HomeFragmentDirections.homeFragmentToSearchFragment()
                )
            }
            else -> throw IllegalArgumentException(
                "Drawer item is not specified in onNavigationItemSelected"
            )
        }
        return true
    }
}
