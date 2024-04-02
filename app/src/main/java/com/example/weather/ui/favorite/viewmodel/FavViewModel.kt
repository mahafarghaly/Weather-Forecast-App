package com.example.weather.ui.favorite.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.model.repo.WeatherRepository
import com.example.weather.model.entity.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavViewModel (private val _repo: WeatherRepository) : ViewModel() {
    private val _weather = MutableLiveData<List<WeatherResponse>>()
    val weather: LiveData<List<WeatherResponse>> get() = _weather

init {
    getStoredWeather()
}

    fun addFav(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                _repo.insertWeather(lat, lon)
                getStoredWeather()
            } catch (e: Exception) {
                println(e)
            }
        }
    }
    fun deleteFav(city: WeatherResponse){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.deleteWeather(city)
            getStoredWeather()
        }
    }
    fun getStoredWeather(){
        viewModelScope.launch {
            _repo.getStoredWeather().collect {
                _weather.value = it
            }
        }
    }
}