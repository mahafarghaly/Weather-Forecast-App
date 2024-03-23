package com.example.weather.utils

import com.example.weather.R

fun getWeatherIconResourceId(iconName: String): Int {
    return when (iconName) {
        "01d" -> R.drawable.dclearsky
        "01n" -> R.drawable.nclearsky
        "02d" -> R.drawable.dfewclouds
        "02n" -> R.drawable.nfewclouds
        "03d" -> R.drawable.scatteredclouds
        "03n" -> R.drawable.scatteredclouds
        "04d" -> R.drawable.brokenclouds
        "04n" -> R.drawable.brokenclouds
        "09d" -> R.drawable.showerrain
        "09n" -> R.drawable.showerrain
        "10d" -> R.drawable.drain
        "10n" -> R.drawable.nrain
        "11d" -> R.drawable.thunderstorm
        "11n" -> R.drawable.thunderstorm
        "13d" -> R.drawable.foggy
        "13n" -> R.drawable.foggy
        else -> R.drawable.stars
    }
}