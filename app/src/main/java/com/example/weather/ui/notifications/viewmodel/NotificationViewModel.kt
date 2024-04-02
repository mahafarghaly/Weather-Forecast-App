package com.example.weather.ui.notifications.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.model.entity.AlarmEntity
import com.example.weather.model.repo.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
class NotificationViewModel(private val _repo: WeatherRepository) : ViewModel() {
    private val _weather = MutableStateFlow<List<AlarmEntity>>(emptyList())
    val weather: StateFlow<List<AlarmEntity>> get() = _weather

    init {
        getStoredAlarm()
    }

    fun addAlarm(lat: Double, lon: Double, time: Long) {
        viewModelScope.launch {
            try {
                _repo.insertAlarm(lat, lon, time)
                getStoredAlarm()
            } catch (e: Exception) {
                println(e)
            }
        }
    }
    fun deletealarm(alarm: AlarmEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            _repo.deleteAlarm(alarm)
            getStoredAlarm()
        }
    }
    private fun getStoredAlarm() {
        viewModelScope.launch {
            _repo.getStoredAlarm().collect {
                _weather.value = it
            }
        }
    }
}