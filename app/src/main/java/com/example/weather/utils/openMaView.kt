package com.example.weather.utils

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.example.weather.R
import com.example.weather.ui.favorite.view.MapsFragment

 fun openMapView(fragmentManager: FragmentManager, source: String,long: Double, land: Double) {
    val fragment = MapsFragment().apply {
        arguments = Bundle().apply {
            putString("source", source)
            putDouble("long", long)
            putDouble("land", land)
        }
    }
    fragmentManager.beginTransaction()
        .replace(R.id.fragment_container, fragment)
        .addToBackStack(null)
        .commit()
}