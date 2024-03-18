package com.example.weather.ui.favorite.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weather.R
import com.example.weather.databinding.FragmentFavoriteBinding


class FavoriteFragment : Fragment() {
    lateinit var binding: FragmentFavoriteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        binding.floatingActionButton.setOnClickListener {
            openMapView(-34.0, 151.0)
        }
        return binding.root

    }


    private fun openMapView(long: Double, land: Double) {
        val fragment = MapsFragment().apply {
            arguments = Bundle().apply {
                putDouble("long", long)
                putDouble("land", land)
            }
        }
        requireActivity().supportFragmentManager.beginTransaction()
        .replace(R.id.fragment_container, fragment)
        .addToBackStack(null)
        .commit()
    }
}