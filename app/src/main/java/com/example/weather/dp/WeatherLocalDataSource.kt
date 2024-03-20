package com.example.weather.dp

import com.example.weather.model.weather.WeatherItem
import com.example.weather.model.weather.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface WeatherLocalDataSource {
    suspend fun insetWeather(weather: WeatherResponse)
    suspend fun deleteProduct(weather: WeatherResponse)
    suspend fun getStoredProducts(): Flow<List<WeatherResponse>>
}