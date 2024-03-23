package com.example.weather.model.fake

import com.example.weather.dp.WeatherLocalDataSource
import com.example.weather.model.weather.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeWeatherLocalDataSource : WeatherLocalDataSource {
    private val weatherList = mutableListOf<WeatherResponse>()

    override suspend fun insetWeather(weather: WeatherResponse) {
        weatherList.add(weather)
    }

    override suspend fun deleteProduct(weather: WeatherResponse) {
        weatherList.remove(weather)
    }

    override suspend fun getStoredWeather(): Flow<List<WeatherResponse>> {
        return flow {
            emit(weatherList)
        }
    }
}