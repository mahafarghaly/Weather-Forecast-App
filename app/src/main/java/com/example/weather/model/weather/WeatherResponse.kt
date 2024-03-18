package com.example.weather.model.weather

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_table")
data class WeatherResponse(
    @PrimaryKey(autoGenerate = true)
   val id: Int = 0,
    val list: List<WeatherItem>,
    val city:City
)




