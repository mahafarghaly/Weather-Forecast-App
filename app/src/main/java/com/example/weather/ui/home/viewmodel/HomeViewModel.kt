package com.example.weather.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.model.repo.WeatherRepository
import com.example.weather.network.ApiState
import com.example.weather.utils.APIKEY
import com.example.weather.utils.LANGUAGE
import com.example.weather.utils.UNITS
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel (private val _repo: WeatherRepository) : ViewModel() {
    private val _weather = MutableStateFlow<ApiState>(ApiState.Loading)
    val weather = _weather.asStateFlow()
fun getWeather(lat: Double, lon: Double) {
    viewModelScope.launch {
        _repo.getWeather(lat, lon, APIKEY, UNITS, LANGUAGE)
            .catch { e ->
                _weather.value = ApiState.Failure(e)
            }.collect { data ->
                _weather.value = ApiState.Success(data)
            }

    }

}
}