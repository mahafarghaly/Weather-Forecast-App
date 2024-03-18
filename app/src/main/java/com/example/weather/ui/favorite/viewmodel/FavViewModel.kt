package com.example.weather.ui.favorite.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.model.repo.WeatherRepository
import com.example.weather.model.weather.WeatherResponse
import kotlinx.coroutines.launch

class FavViewModel (private val _repo: WeatherRepository) : ViewModel() {
    private val _weather = MutableLiveData<WeatherResponse>()
    val weather: LiveData<WeatherResponse> get() = _weather

    fun addFav(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val weatherResponse = _repo.insertWeather(lat,lon)
                _weather.postValue(weatherResponse)
            } catch (e: Exception) {

                println(e)
            }
        }
    }
}