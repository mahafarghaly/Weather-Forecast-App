package com.example.weather.model

data class WeatherResponse(
    val cod: String,
    val message: Int,
    val cnt: Int,
    val list: List<WeatherItem>
)

data class Clouds(
    val all: Int
)

data class Sys(
    val pod: String
)
