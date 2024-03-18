package com.example.weather.ui.favorite

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
import com.example.weather.R
import com.example.weather.databinding.FragmentMapsBinding

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
    private lateinit var searchEditText: EditText
    private lateinit var searchButton: ImageButton
    private lateinit var binding: FragmentMapsBinding
    private  val TAG = "MapsFragment"
    private var isSnackbarShown: Boolean = false
    private lateinit var snackbar: Snackbar
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

        binding.searchButton.setOnClickListener {
            val locationName = binding.searchEditText.text.toString()

                val geocoder = Geocoder(requireContext(), Locale.getDefault())
                val addresses = geocoder.getFromLocationName(locationName, 1)
                if (addresses != null && addresses.isNotEmpty()) {
                    val latLng = LatLng(addresses[0].latitude, addresses[0].longitude)
                    Log.i(TAG, "latitude: ${addresses[0].latitude}")
                    Log.i(TAG, "longitude: ${addresses[0].longitude}")
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

               // saveLocationToRoom()
                isSnackbarShown = false
                snackbar.dismiss()
            }
            snackbar.view.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.colorPrimaryLight))
            snackbar.show()
            isSnackbarShown = true
        }
    }

}

