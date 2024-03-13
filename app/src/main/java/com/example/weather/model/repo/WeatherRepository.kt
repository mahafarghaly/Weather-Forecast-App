package com.example.weather.model.repo

import com.example.weather.model.weather.WeatherItem

interface WeatherRepository {
    suspend fun getWeather(lat: Double,lon: Double,apiKey: String,units: String,lang: String): List<WeatherItem>
}