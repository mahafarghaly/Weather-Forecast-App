package com.example.weather.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weather.model.weather.City
import com.example.weather.model.weather.WeatherItem
import java.io.Serializable

@Entity(tableName = "favorite_table")
data class WeatherResponse(
    @PrimaryKey
   val city: City,
    val list: List<WeatherItem>,

    ) : Serializable




