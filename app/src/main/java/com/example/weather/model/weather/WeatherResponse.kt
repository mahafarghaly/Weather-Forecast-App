package com.example.weather.model.weather

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "favorite_table")
data class WeatherResponse(
   @PrimaryKey
   val city:City,
    val list: List<WeatherItem>,

) : Serializable




