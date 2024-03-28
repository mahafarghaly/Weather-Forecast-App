package com.example.weather.ui.notifications.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.model.entity.AlarmEntity
import com.example.weather.model.entity.WeatherResponse
import com.example.weather.model.repo.WeatherRepository
import com.example.weather.network.ApiState
import com.example.weather.utils.APIKEY
import com.example.weather.utils.LANGUAGE
import com.example.weather.utils.UNITS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class NotificationViewModel (private val _repo: WeatherRepository) : ViewModel() {
    private val _weather = MutableLiveData<List<AlarmEntity>>()
    val weather: LiveData<List<AlarmEntity>> get() = _weather

    init {
        getStoredAlarm()
    }

    fun addAlarm(lat: Double, lon: Double,time:Long) {
        viewModelScope.launch {
            try {
                _repo.insertAlarm(lat,lon,time)
               getStoredAlarm() // Reload the weather list after insertion
            } catch (e: Exception) {
                println(e)
            }
        }
    }
    fun deletealarm(alarm: AlarmEntity){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.deleteAlarm(alarm)
            getStoredAlarm()
        }
    }
    fun getStoredAlarm(){
        viewModelScope.launch {
            _repo.getStoredAlarm().collect {
                _weather.value = it
            }
        }
    }
}