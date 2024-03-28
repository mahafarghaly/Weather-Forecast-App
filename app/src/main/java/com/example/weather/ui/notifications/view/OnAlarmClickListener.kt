package com.example.weather.ui.notifications.view

import com.example.weather.model.entity.AlarmEntity

interface OnAlarmClickListener {
    fun onAlarmClick(fav: AlarmEntity?)
}