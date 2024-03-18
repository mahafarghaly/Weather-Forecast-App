package com.example.weather.ui.home.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.weather.R
import com.example.weather.databinding.FragmentHomeBinding
import com.example.weather.model.repo.WeatherRepositoryImpl
import com.example.weather.network.WeatherRemoteDataSourceImpl
import com.example.weather.ui.favorite.MapsFragment
import com.example.weather.ui.home.viewmodel.HomeViewModel
import com.example.weather.ui.home.viewmodel.HomeViewModelFactory
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.text.SimpleDateFormat
import java.util.Locale

class HomeFragment : Fragment() {

    private lateinit var homeFactory: HomeViewModelFactory
    private lateinit var viewModel: HomeViewModel
    private  val TAG = "HomeFragment"
lateinit var binding:FragmentHomeBinding
lateinit var dayHourAdapter: DayHourAdapter
    private lateinit var layoutmanager1: LinearLayoutManager
    private lateinit var layoutmanager2: LinearLayoutManager
    lateinit var daysAdapter: DaysAdapter

val locationRequestId=5
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val repository = WeatherRepositoryImpl.getInstance(
            WeatherRemoteDataSourceImpl.getInstance(),
            //ProductsLocalDataSourceImpl.getInstance(this)
        )

        var fusedClient= LocationServices.getFusedLocationProviderClient(requireContext())
        var locationRequest: LocationRequest = LocationRequest.
        Builder(1000).apply {
            setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
        }.build()
        var callback: LocationCallback =object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {
                Toast.makeText(requireContext(),locationResult.lastLocation.toString(), Toast.LENGTH_SHORT).show()
                val location=locationResult.lastLocation
                Log.i(TAG, "latitude : ${location?.latitude}")
                Log.i(TAG, "longtiude:${location?.longitude} ")
                location?.let {
                    viewModel.getWeather(it.latitude, it.longitude)
                }
                fusedClient.removeLocationUpdates(this)
            }
        }
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(
              requireActivity(),
                arrayOf( Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                locationRequestId
            )
            return
        }
        fusedClient.requestLocationUpdates(locationRequest,callback, Looper.myLooper())




        //*****************************************
        homeFactory = HomeViewModelFactory(repository)
        viewModel = ViewModelProvider(this, homeFactory).get(HomeViewModel::class.java)

        viewModel.weather.observe(this, Observer { weatherState ->
          //  allProductsAdapter.setProductsList(products)
            dayHourAdapter.setDayList(weatherState.list)
            daysAdapter.setDayList(weatherState.list)

            Log.i(TAG, "weather response:: $weatherState")
            binding.tvCity.text=weatherState.city.name
            binding.tvWeatherState.text=weatherState.list.get(0).weather.get(0).description.capitalizeWords()
            binding.tvTemp.text=weatherState.list.get(0).main.temp_min.toInt().toString()+"°C"
           binding.tvPressure.text=weatherState.list.get(0).main.pressure.toString()
            binding.tvHum.text=weatherState.list.get(0).main.humidity.toString()
            binding.tvWind.text=weatherState.list.get(0).wind.speed.toString()
            binding.tvCloud.text=weatherState.list.get(0).clouds.all.toString()
            binding.tvViss.text=weatherState.list.get(0).visibility.toString()
            binding.tvSea.text=weatherState.list.get(0).main.sea_level.toString()
            //binding.tvDate
            val inputDate =weatherState.list.get(0).dt_txt
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

            try {
                val date = inputFormat.parse(inputDate)

                val outputFormat = SimpleDateFormat("EEEE,dd MMMM, HH:mm", Locale.getDefault())
                val formattedDate = outputFormat.format(date)

                binding.tvDate.text=formattedDate
                println("Converted date: $formattedDate")
            } catch (e: Exception) {
                e.printStackTrace()
            }

//            Glide.with(requireContext())
//                .load("https://openweathermap.org/img/wn/${weatherState.list[0].weather[0].icon}@2x.png")
//                .apply(RequestOptions()
//                    )
//                .into(binding.ivWeather)

           //  this code replace with static icon
            val iconName = weatherState.list[0].weather[0].icon
            val iconResourceId = getWeatherIconResourceId(iconName)
            Glide.with(requireContext())
                .load(iconResourceId)
                .apply(RequestOptions()

                )
                .into(binding.ivWeather)


        })
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ){
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf( Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                locationRequestId
            )
        }else{
            var fusedClient= LocationServices.getFusedLocationProviderClient(requireContext())
            var locationRequest:LocationRequest=LocationRequest.Builder(1000).apply {
                setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
            }.build()
            var callback:LocationCallback=object :LocationCallback(){
                override fun onLocationResult(locationResult: LocationResult) {
                    Toast.makeText(requireContext(),locationResult.lastLocation.toString(),Toast.LENGTH_SHORT).show()
                    val location=locationResult.lastLocation
                    location?.let {
                        viewModel.getWeather(it.latitude, it.longitude)
                    }

                }
            }
            fusedClient.requestLocationUpdates(locationRequest,callback, Looper.myLooper())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentHomeBinding.inflate(inflater,container,false)
        layoutmanager1  = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.rvHours.layoutManager = layoutmanager1
        dayHourAdapter = DayHourAdapter(requireContext())
        binding.rvHours.adapter = dayHourAdapter
        // list 2

        layoutmanager2 = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.rvDays.layoutManager = layoutmanager2
        daysAdapter = DaysAdapter(requireContext())
        binding.rvDays.adapter = daysAdapter
        return binding.root
    }

    fun getWeatherIconResourceId(iconName: String): Int {
        return when (iconName) {
//            "01d" -> R.drawable.ic_weather_clear_sky_day
//            "01n" -> R.drawable.ic_weather_clear_sky_night
//            "02d" -> R.drawable.ic_weather_few_clouds_day
            "01d" -> R.drawable.sun
            "01n" -> R.drawable.moon
            // Add more mappings for other icon names
            else -> R.drawable.favorite
        }
    }

    fun String.capitalizeWords(): String {
        return this.split(" ").joinToString(" ") { it.capitalize() }
    }

}