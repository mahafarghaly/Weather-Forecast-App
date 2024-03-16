package com.example.weather.network

import com.example.weather.model.weather.WeatherItem
import com.example.weather.model.weather.WeatherResponse

interface WeatherRemoteDataSource {
    suspend fun getWeatherOverNetwork(lat: Double,lon: Double,apiKey: String,units: String,lang: String): WeatherResponse
}
/*
  @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String,//lang
        @Query("lang") lang: String
 */