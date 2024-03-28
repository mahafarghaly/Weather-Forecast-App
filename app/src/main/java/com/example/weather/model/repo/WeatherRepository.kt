package com.example.weather.model.repo

import com.example.weather.model.entity.AlarmEntity
import com.example.weather.model.entity.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getWeather(lat: Double,lon: Double,apiKey: String,units: String,lang: String): Flow<WeatherResponse>
    suspend fun getStoredWeather(): Flow<List<WeatherResponse>>
    suspend fun insertWeather(late:Double,long:Double): WeatherResponse
    suspend fun deleteWeather(weather: WeatherResponse)
    //alarm
    suspend fun getStoredAlarm(): Flow<List<AlarmEntity>>
    suspend fun insertAlarm(late:Double,long:Double,time:Long)
    suspend fun deleteAlarm(alarm: AlarmEntity)
}