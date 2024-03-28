package com.example.weather.utils.format

import android.widget.TextView
import com.example.weather.utils.getTimeFormat
import java.util.TimeZone

fun TextView.setTime(timeInSecond: Int, timeZone: TimeZone) {
    text = getTimeFormat(timeInSecond * 1000L,timeZone)
}
fun TextView.setTime(timeInMilliSecond: Long) {
    text = getTimeFormat(timeInMilliSecond)
}