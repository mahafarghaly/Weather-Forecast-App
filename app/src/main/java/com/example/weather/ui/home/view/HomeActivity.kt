package com.example.weather.ui.home.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.weather.R
import com.example.weather.dp.WeatherLocalDataSource
import com.example.weather.dp.WeatherLocalDataSourceImpl
import com.example.weather.model.repo.WeatherRepositoryImpl
import com.example.weather.network.WeatherRemoteDataSourceImpl
import com.example.weather.ui.home.viewmodel.HomeViewModel
import com.example.weather.ui.home.viewmodel.HomeViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    private val TAG = "HomeActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        bottomNav.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment)
                    Log.i(TAG, "Home Fragment")
                }

                R.id.favoriteFragment -> {
                    navController.navigate(R.id.favoriteFragment)
                    Log.i(TAG, "Favorite Fragment")
                }

                R.id.notificationsFragment -> {
                    navController.navigate(R.id.notificationsFragment)
                    Log.i(TAG, "Notifications Fragment")
                }

                R.id.settingsFragment -> {
                    navController.navigate(R.id.settingsFragment)
                    Log.i(TAG, "Settings Fragment")
                }
            }
            true
        }
    }
}