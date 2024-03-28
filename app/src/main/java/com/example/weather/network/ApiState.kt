package com.example.weather.network

import com.example.weather.model.entity.WeatherResponse

sealed class ApiState {
    class Success(val data: WeatherResponse): ApiState()
    class Failure(val msg:Throwable): ApiState()
    object Loading: ApiState()
}