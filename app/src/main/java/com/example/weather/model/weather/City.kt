package com.example.weather.model.weather

import androidx.room.PrimaryKey

data class City(
    val name: String,
    val country:String,
    val coord:Coord

)