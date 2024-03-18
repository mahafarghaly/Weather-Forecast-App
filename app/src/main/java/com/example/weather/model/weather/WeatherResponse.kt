package com.example.weather.model.weather

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_table")
data class WeatherResponse(
    @PrimaryKey val id: Int = 1,
    val cod: String,
    val message: Int,
    val cnt: Int,
    val list: List<WeatherItem>,
    val city:City
)




