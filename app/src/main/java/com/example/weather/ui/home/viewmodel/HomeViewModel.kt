package com.example.weather.ui.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.model.repo.WeatherRepository
import com.example.weather.model.weather.WeatherItem
import kotlinx.coroutines.launch

class HomeViewModel (private val _repo: WeatherRepository) : ViewModel() {
    private val _weather = MutableLiveData<List<WeatherItem>>()
    //MutableLiveData<List<Product>>(emptyList())
    val weather: LiveData<List<WeatherItem>> get() = _weather

    init {
        val lat = 44.34
        val lon = 10.99
        val apiKey = "5821a38d0f5b56ff77dcd5d5a0862647"
        val units="metric"
        val lang="en"

        getWeather(lat, lon, apiKey, units, lang)
    }


    fun getWeather(lat: Double,lon: Double,apiKey: String,units: String,lang: String){
      viewModelScope.launch {
          val weatherList = _repo.getWeather(lat,lon,apiKey,units,lang)
          _weather.postValue(weatherList)

      }
  }


}