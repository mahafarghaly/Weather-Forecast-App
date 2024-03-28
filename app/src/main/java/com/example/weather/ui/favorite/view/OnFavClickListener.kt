package com.example.weather.ui.favorite.view

import com.example.weather.model.entity.WeatherResponse

interface OnFavClickListener {
    fun onFavClick(fav: WeatherResponse?)
}
