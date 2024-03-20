package com.example.weather.ui.favorite.view

import com.example.weather.model.weather.WeatherResponse

interface OnFavClickListener {
    fun onFavClick(fav:WeatherResponse?)
}
