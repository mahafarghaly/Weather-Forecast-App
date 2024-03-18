package com.example.weather.model.repo

import com.example.weather.model.weather.WeatherItem
import com.example.weather.model.weather.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getWeather(lat: Double,lon: Double,apiKey: String,units: String,lang: String): WeatherResponse
    suspend fun getStoredWeather(): Flow<List<WeatherItem>>
    suspend fun insertWeather(late:Double,long:Double):WeatherResponse
    suspend fun deleteWeather(weather: WeatherResponse)
}