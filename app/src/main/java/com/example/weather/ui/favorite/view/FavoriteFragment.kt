package com.example.weather.ui.favorite.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.weather.R
import com.example.weather.databinding.FragmentFavoriteBinding
import com.example.weather.dp.WeatherLocalDataSourceImpl
import com.example.weather.model.repo.WeatherRepositoryImpl
import com.example.weather.model.entity.WeatherResponse
import com.example.weather.network.WeatherRemoteDataSourceImpl
import com.example.weather.ui.favorite.viewmodel.FavViewModel
import com.example.weather.ui.favorite.viewmodel.FavViewModelFactory
import com.example.weather.utils.openMapView


class FavoriteFragment : Fragment(),OnFavClickListener {
    lateinit var binding: FragmentFavoriteBinding
    lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var layoutManager:LinearLayoutManager
    lateinit var favViewMode:FavViewModel
    lateinit var  favFactory :FavViewModelFactory
    private  val TAG = "FavoriteFragment"
    private lateinit var lottieAnimationView:LottieAnimationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = WeatherRepositoryImpl.getInstance(
            WeatherRemoteDataSourceImpl.getInstance(),
            WeatherLocalDataSourceImpl.getInstance(requireContext())
        )
        favFactory = FavViewModelFactory(repository)
        favViewMode = ViewModelProvider(this, favFactory).get(FavViewModel::class.java)

        favViewMode.weather.observe(this, Observer {
             weatherState  ->

            favoriteAdapter.setCityList(weatherState)
            Log.i(TAG, "list counts: ${favoriteAdapter.itemCount}")

            updateLottieVisibility()
    })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        binding.floatingActionButton.setOnClickListener {
            openMapView(requireActivity().supportFragmentManager,"favorite",-34.0, 151.0)
            lottieAnimationView.visibility = View.GONE
        }
        layoutManager= LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
       binding.rvFavorite.layoutManager=layoutManager
        favoriteAdapter= FavoriteAdapter(requireContext(),this)
        binding.rvFavorite.adapter=favoriteAdapter
        lottieAnimationView = binding.lottieAnimationView
       lottieAnimationView.playAnimation()
        updateLottieVisibility()
        return binding.root

    }



    override fun onFavClick(fav: WeatherResponse?) {
        fav?.let { favViewMode.deleteFav(it) }
        Toast.makeText(requireContext(), " removed from favorite", Toast.LENGTH_SHORT).show()

    }
    private fun updateLottieVisibility() {
        if (favoriteAdapter.itemCount > 0) {
            lottieAnimationView.visibility = View.GONE
        } else {
            lottieAnimationView.visibility = View.VISIBLE
        }
    }
    }
