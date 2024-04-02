package com.example.weather.network

import com.example.weather.model.entity.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface WeatherRemoteDataSource {
    suspend fun getWeatherOverNetwork(lat: Double,lon: Double,apiKey: String,units: String,lang: String): Flow<WeatherResponse>
}
