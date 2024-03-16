package com.example.weather.model.weather

data class WeatherResponse(
    val cod: String,
    val message: Int,
    val cnt: Int,
    val list: List<WeatherItem>,
    val city:City
)
data class City(
    val name: String,
    val country:String
)

data class Clouds(
    val all: Int
)

data class Sys(
    val pod: String
)
