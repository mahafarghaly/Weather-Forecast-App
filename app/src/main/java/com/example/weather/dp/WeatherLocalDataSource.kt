package com.example.weather.dp

import com.example.weather.model.entity.AlarmEntity
import com.example.weather.model.entity.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface WeatherLocalDataSource {
    suspend fun insetWeather(weather: WeatherResponse)
    suspend fun deleteWeather(weather: WeatherResponse)
    suspend fun getStoredWeather(): Flow<List<WeatherResponse>>
    //alarm
    suspend fun insetAlarm(alarm:AlarmEntity)
    suspend fun deleteAlarm(alarm:AlarmEntity)
    suspend fun getStoredAlarm(): Flow<List<AlarmEntity>>
}