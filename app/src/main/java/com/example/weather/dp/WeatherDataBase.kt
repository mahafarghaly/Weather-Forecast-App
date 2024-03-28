package com.example.weather.dp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weather.model.entity.AlarmEntity
import com.example.weather.model.entity.WeatherResponse

@Database(entities = arrayOf(WeatherResponse::class ,AlarmEntity::class), version = 4)
@TypeConverters(Converters::class)
abstract class WeatherDataBase : RoomDatabase(){
    abstract  fun getWeatherDao():WeatherDAO
    abstract  fun getAlarmDao():AlarmDAO
    companion object{
        @Volatile
        private  var INSTANCE:WeatherDataBase?=null
        fun getInstance(ctx: Context):WeatherDataBase{
            return INSTANCE?:synchronized(this){
                val instance= Room.databaseBuilder(
                    ctx.applicationContext,WeatherDataBase::class.java,"weather_database")
                    .fallbackToDestructiveMigration().build()
                INSTANCE=instance
                instance
            }
        }
    }
}