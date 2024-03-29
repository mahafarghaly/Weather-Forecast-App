package com.example.weather.model.fake

import com.example.weather.dp.WeatherLocalDataSource
import com.example.weather.model.entity.AlarmEntity
import com.example.weather.model.entity.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeWeatherLocalDataSource : WeatherLocalDataSource {
    private val weatherList = mutableListOf<WeatherResponse>()
    private val alarmList = mutableListOf<AlarmEntity>()

    override suspend fun insetWeather(weather: WeatherResponse) {
        weatherList.add(weather)
    }

    override suspend fun deleteWeather(weather: WeatherResponse) {
        weatherList.remove(weather)
    }

    override suspend fun getStoredWeather(): Flow<List<WeatherResponse>> {
        return flow {
            emit(weatherList)
        }
    }

    override suspend fun insetAlarm(alarm: AlarmEntity) {
   alarmList.add(alarm)
    }

    override suspend fun deleteAlarm(alarm: AlarmEntity) {
alarmList.remove(alarm)
    }

    override suspend fun getStoredAlarm(): Flow<List<AlarmEntity>> {
    return flow {
        emit(alarmList)
    }
    }
}