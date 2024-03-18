package com.example.weather.ui.favorite.view

import android.content.res.ColorStateList
import android.location.Geocoder
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.weather.R
import com.example.weather.databinding.FragmentMapsBinding
import com.example.weather.dp.WeatherLocalDataSourceImpl
import com.example.weather.model.repo.WeatherRepository
import com.example.weather.model.repo.WeatherRepositoryImpl
import com.example.weather.network.WeatherRemoteDataSourceImpl
import com.example.weather.ui.favorite.viewmodel.FavViewModel
import com.example.weather.ui.favorite.viewmodel.FavViewModelFactory
import com.example.weather.ui.home.viewmodel.HomeViewModel
import com.example.weather.ui.home.viewmodel.HomeViewModelFactory

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import java.util.Locale

class MapsFragment : Fragment() {
    private lateinit var map: GoogleMap

    private lateinit var binding: FragmentMapsBinding
    private  val TAG = "MapsFragment"
    private var isSnackbarShown: Boolean = false
    private lateinit var snackbar: Snackbar
    private lateinit var favFactory: FavViewModelFactory
    private lateinit var viewModel: FavViewModel
     var latitude:Double=0.0
    var longitude:Double=0.0

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        map.uiSettings.isZoomControlsEnabled = true

        val defaultLocation = LatLng(-34.0, 151.0)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10f))

        map.addMarker(MarkerOptions().position(defaultLocation).title("Marker in Sydney"))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        val repository = WeatherRepositoryImpl.getInstance(
            WeatherRemoteDataSourceImpl.getInstance(),
            WeatherLocalDataSourceImpl.getInstance(requireContext())
        )
        favFactory = FavViewModelFactory(repository)
        viewModel = ViewModelProvider(this, favFactory).get(FavViewModel::class.java)

        binding.searchButton.setOnClickListener {
            val locationName = binding.searchEditText.text.toString()

                val geocoder = Geocoder(requireContext(), Locale.getDefault())
                val addresses = geocoder.getFromLocationName(locationName, 1)
                if (addresses != null && addresses.isNotEmpty()) {

                    latitude=addresses[0].latitude
                    longitude=addresses[0].longitude
                    val latLng = LatLng(latitude, longitude)
                    Log.i(TAG, "latitude: $latitude")
                    Log.i(TAG, "longitude: $longitude")
                    moveMapToLocation(latLng)
                    showSaveLocationSnackbar()


                }

        }
        return binding.root
    }

    private fun moveMapToLocation(latLng: LatLng) {
        map.clear()
        map.addMarker(MarkerOptions().position(latLng).title("Marker"))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

    }
    private fun showSaveLocationSnackbar() {
        if (!isSnackbarShown) {
            snackbar = Snackbar.make(
                binding.root,
                "Save location in Room database?",
                Snackbar.LENGTH_INDEFINITE
            )
            snackbar.setAction("Save") {

                viewModel.addFav(latitude,longitude)
                isSnackbarShown = false
                snackbar.dismiss()
                val favoriteFragment = FavoriteFragment()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, favoriteFragment)
                    .addToBackStack(null)
                    .commit()

            }
            snackbar.view.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.colorPrimaryLight))
            snackbar.show()
            isSnackbarShown = true
        }
    }
//    private fun saveLocationToRoom() {
//        val locationName = binding.searchEditText.text.toString()
//        val latLng = LatLng(addresses[0].latitude, addresses[0].longitude)
//        val city = City(name = locationName, lat = latLng.latitude, lon = latLng.longitude)
//        viewModel.addFav(city)
//    }

}

