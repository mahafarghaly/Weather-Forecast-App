package com.example.weather.ui.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.model.repo.WeatherRepository
import com.example.weather.model.weather.WeatherItem
import com.example.weather.model.weather.WeatherResponse
import com.example.weather.network.ApiState
import com.example.weather.utilts.APIKEY
import com.example.weather.utilts.LANGUAGE
import com.example.weather.utilts.UNITS
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel (private val _repo: WeatherRepository) : ViewModel() {
//    private val _weather = MutableLiveData<WeatherResponse>()
//    val weather: LiveData<WeatherResponse> get() = _weather
    private val _weather = MutableStateFlow<ApiState>(ApiState.Loading)
    val weather = _weather.asStateFlow()
fun getWeather(lat: Double, lon: Double) {
//    viewModelScope.launch {
//        val weatherList = _repo.getWeather(lat, lon, APIKEY, UNITS, LANGUAGE)
//        _weather.postValue(weatherList)
//    }
    viewModelScope.launch {
        _repo.getWeather(lat, lon, APIKEY, UNITS, LANGUAGE)
            .catch { e ->
                _weather.value = ApiState.Failure(e)
            }.collect { data ->
                _weather.value = ApiState.Success(data)
            }

    }

}
//    fun getWeather(lat: Double, lon: Double, isFromFavorite: Boolean = false, favWeatherResponse: WeatherResponse?=null) {
//        viewModelScope.launch {
//            if (isFromFavorite && favWeatherResponse != null) {
//                // Use data from favorite location
//                _weather.postValue(favWeatherResponse!!)
//            } else {
//                // Use data from current location
//                val weatherList = _repo.getWeather(lat, lon, APIKEY, UNITS, LANGUAGE)
//                _weather.postValue(weatherList)
//            }
//        }
//    }


}