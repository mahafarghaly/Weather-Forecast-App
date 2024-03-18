package com.example.weather.dp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weather.model.weather.WeatherResponse

@Database(entities = arrayOf(WeatherResponse::class), version = 2)
@TypeConverters(Converters::class)
abstract class WeatherDataBase : RoomDatabase(){
    abstract  fun getWeatherDao():WeatherDAO
    companion object{
        @Volatile
        private  var INSTANCE:WeatherDataBase?=null
        fun getInstance(ctx: Context):WeatherDataBase{
            return INSTANCE?:synchronized(this){
                val instance= Room.databaseBuilder(
                    ctx.applicationContext,WeatherDataBase::class.java,"weather_database")
                    .build()
                INSTANCE=instance
                instance
            }
        }
    }
}