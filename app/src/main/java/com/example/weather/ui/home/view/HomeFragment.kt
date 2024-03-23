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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.weather.R
import com.example.weather.databinding.FragmentHomeBinding
import com.example.weather.dp.WeatherLocalDataSourceImpl
import com.example.weather.model.repo.WeatherRepositoryImpl
import com.example.weather.model.weather.WeatherResponse
import com.example.weather.network.ApiState
import com.example.weather.network.WeatherRemoteDataSourceImpl
import com.example.weather.ui.favorite.viewmodel.FavViewModel
import com.example.weather.ui.favorite.viewmodel.FavViewModelFactory
import com.example.weather.ui.home.viewmodel.HomeViewModel
import com.example.weather.ui.home.viewmodel.HomeViewModelFactory
import com.example.weather.utils.getWeatherIconResourceId
import com.example.weather.utils.units.setTemp
import com.example.weather.utils.units.setWindSpeed
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.roundToInt

class HomeFragment : Fragment() {

    private lateinit var homeFactory: HomeViewModelFactory
    private lateinit var viewModel: HomeViewModel
    private lateinit var favFactory: FavViewModelFactory
    private lateinit var favViewModel: FavViewModel
    private val TAG = "HomeFragment"
    lateinit var binding: FragmentHomeBinding
    lateinit var dayHourAdapter: DayHourAdapter
    private lateinit var layoutmanager1: LinearLayoutManager
    private lateinit var layoutmanager2: LinearLayoutManager
    lateinit var daysAdapter: DaysAdapter
    var weatherState: WeatherResponse? = null
    var cityName: String? = ""
    var clickedItemIndex: Int = 0
    val locationRequestId = 5
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weatherState = arguments?.getSerializable("weatherResponse") as? WeatherResponse
        clickedItemIndex = arguments?.getInt("clickedItemIndex") ?: 0
        cityName = arguments?.getString("cityName")
        Log.i(TAG, "data from fav:${weatherState?.city?.coord?.lon}")
        Log.i(TAG, "data from fav:${weatherState?.city?.coord?.lat}")

        val repository = WeatherRepositoryImpl.getInstance(
            WeatherRemoteDataSourceImpl.getInstance(),
            WeatherLocalDataSourceImpl.getInstance(requireContext())
        )

        var fusedClient = LocationServices.getFusedLocationProviderClient(requireContext())
        var locationRequest: LocationRequest = LocationRequest.Builder(1000).apply {
            setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
        }.build()
        var callback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                Toast.makeText(
                    requireContext(),
                    locationResult.lastLocation.toString(),
                    Toast.LENGTH_SHORT
                ).show()
                val location = locationResult.lastLocation
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
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                locationRequestId
            )
            return
        }
        fusedClient.requestLocationUpdates(locationRequest, callback, Looper.myLooper())
        homeFactory = HomeViewModelFactory(repository)
        viewModel = ViewModelProvider(this, homeFactory).get(HomeViewModel::class.java)

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
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                locationRequestId
            )
        } else {
            var fusedClient = LocationServices.getFusedLocationProviderClient(requireContext())
            var locationRequest: LocationRequest = LocationRequest.Builder(1000).apply {
                setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
            }.build()
            var callback: LocationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    Toast.makeText(
                        requireContext(),
                        locationResult.lastLocation.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    val location = locationResult.lastLocation
                    location?.let {

                        if (weatherState != null) {
                            viewModel.getWeather(
                                weatherState!!.city.coord.lat,
                                weatherState!!.city.coord.lon
                            )
                        } else {
                            viewModel.getWeather(it.latitude, it.longitude)
                        }

                    }

                }
            }
            fusedClient.requestLocationUpdates(locationRequest, callback, Looper.myLooper())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        layoutmanager1 = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.rvHours.layoutManager = layoutmanager1
        dayHourAdapter = DayHourAdapter(requireContext())
        binding.rvHours.adapter = dayHourAdapter
        // list 2

        layoutmanager2 = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.rvDays.layoutManager = layoutmanager2
        daysAdapter = DaysAdapter(requireContext())
        binding.rvDays.adapter = daysAdapter
        //*****************
        val clickedWeatherItem = weatherState?.list?.getOrNull(clickedItemIndex)

        if (clickedWeatherItem != null) {
            val repository = WeatherRepositoryImpl.getInstance(
                WeatherRemoteDataSourceImpl.getInstance(),
                WeatherLocalDataSourceImpl.getInstance(requireContext())
            )
            favFactory = FavViewModelFactory(repository)
            favViewModel = ViewModelProvider(this, favFactory).get(FavViewModel::class.java)

            favViewModel.weather.observe(viewLifecycleOwner, Observer { weatherState ->
                //  allProductsAdapter.setProductsList(products)
                dayHourAdapter.setDayList(weatherState.get(0).list)
                daysAdapter.setDayList(weatherState.get(0).list)

                binding.progressBar.visibility = View.GONE
                binding.tvCity.text = cityName
                binding.tvWeatherState.text =
                    clickedWeatherItem.weather.get(0).description.capitalizeWords()
                binding.tvTemp.setTemp(
                    clickedWeatherItem.main.temp_min.roundToInt(),
                    context = requireActivity().application
                )

                binding.tvPressure.text = clickedWeatherItem.main.pressure.toString()
                binding.tvHum.text = clickedWeatherItem.main.humidity.toString()
                binding.tvWind.text = clickedWeatherItem.wind.speed.toString()
                binding.tvCloud.text = clickedWeatherItem.clouds.all.toString()
                binding.tvViss.text = clickedWeatherItem.visibility.toString()
                binding.tvSea.text = clickedWeatherItem.main.sea_level.toString()
                binding.tvDate
                val inputDate = clickedWeatherItem.dt_txt
                val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

                try {
                    val date = inputFormat.parse(inputDate)

                    val outputFormat = SimpleDateFormat("EEEE,dd MMMM, HH:mm", Locale.getDefault())
                    val formattedDate = outputFormat.format(date)

                    binding.tvDate.text = formattedDate
                    println("Converted date: $formattedDate")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                val iconName = clickedWeatherItem.weather[0].icon
                val iconResourceId = getWeatherIconResourceId(iconName)
                Glide.with(requireContext())
                    .load(iconResourceId)
                    .apply(
                        RequestOptions()
                    ).into(binding.ivWeather)
            })
        } else {
            lifecycleScope.launch {
                viewModel.weather.collectLatest { result ->
                    when (result) {
                        is ApiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.tvCity.visibility = View.GONE
                            binding.tvWeatherState.visibility = View.GONE
                            binding.tvDate.visibility = View.GONE
                            binding.ivWeather.visibility = View.GONE
                            binding.rvHours.visibility = View.GONE
                            binding.rvDays.visibility = View.GONE
                            binding.cardView.visibility = View.GONE
                            binding.tvTemp.visibility = View.GONE
                        }

                        is ApiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.tvCity.visibility = View.VISIBLE
                            binding.tvWeatherState.visibility = View.VISIBLE
                            binding.tvDate.visibility = View.VISIBLE
                            binding.ivWeather.visibility = View.VISIBLE
                            binding.rvHours.visibility = View.VISIBLE
                            binding.rvDays.visibility = View.VISIBLE
                            binding.cardView.visibility = View.VISIBLE
                            binding.tvTemp.visibility = View.VISIBLE

                            // data

                            dayHourAdapter.setDayList(result.data.list)
                            daysAdapter.setDayList(result.data.list)

                            Log.i(TAG, "weather response:: ${result.data}")
                            binding.tvCity.text = result.data.city.name
                            binding.tvWeatherState.text =
                                result.data.list.get(0).weather.get(0).description.capitalizeWords()
                            binding.tvTemp.setTemp(
                                result.data.list.get(0).main.temp_min.roundToInt(),
                                context = requireActivity().application
                            )
                            //= result.data.list.get(0).main.temp_min.toInt().toString() + "°C"
                            binding.tvPressure.text =
                                result.data.list.get(0).main.pressure.toString()
                            binding.tvHum.text = result.data.list.get(0).main.humidity.toString()
                            binding.tvWind.setWindSpeed(
                                result.data.list.get(0).wind.speed, requireActivity().application
                            )
                            //= result.data.list.get(0).wind.speed.toString()
                            binding.tvCloud.text = result.data.list.get(0).clouds.all.toString()
                            binding.tvViss.text = result.data.list.get(0).visibility.toString()
                            binding.tvSea.text = result.data.list.get(0).main.sea_level.toString()
                            //binding.tvDate
                            val inputDate = result.data.list.get(0).dt_txt
                            val inputFormat =
                                SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

                            try {
                                val date = inputFormat.parse(inputDate)

                                val outputFormat =
                                    SimpleDateFormat("EEEE,dd MMMM, HH:mm", Locale.getDefault())
                                val formattedDate = outputFormat.format(date)

                                binding.tvDate.text = formattedDate
                                println("Converted date: $formattedDate")
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                            val iconName = result.data.list[0].weather[0].icon
                            val iconResourceId = getWeatherIconResourceId(iconName)
                            Glide.with(requireContext())
                                .load(iconResourceId)
                                .apply(
                                    RequestOptions()

                                )
                                .into(binding.ivWeather)


                        }

                        else -> {
                            binding.progressBar.visibility = View.GONE
//                        Toast.makeText(
//                            this@HomeFragment,"error",Toast.LENGTH_SHORT).show()

                        }
                    }
                }
            }
        }

        return binding.root
    }



    fun String.capitalizeWords(): String {
        return this.split(" ").joinToString(" ") { it.capitalize() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}