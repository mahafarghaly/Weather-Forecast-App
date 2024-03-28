package com.example.weather.dp

import android.content.Context
import com.example.weather.model.entity.AlarmEntity
import com.example.weather.model.entity.WeatherResponse
import kotlinx.coroutines.flow.Flow

class WeatherLocalDataSourceImpl (context: Context):WeatherLocalDataSource  {
    private  val dao:WeatherDAO by lazy {
        val db:WeatherDataBase=WeatherDataBase.getInstance(context)
        db.getWeatherDao()
    }
    private  val alarmDao:AlarmDAO by lazy {
        val db:WeatherDataBase=WeatherDataBase.getInstance(context)
        db.getAlarmDao()
    }
    companion object{
        private var instance: WeatherLocalDataSourceImpl?=null
        fun getInstance(_context:Context): WeatherLocalDataSourceImpl {
            return instance?: synchronized(this){
                val temp= WeatherLocalDataSourceImpl(_context)
                instance= temp
                temp
            }
        }
    }
    override suspend fun insetWeather(weather: WeatherResponse) {
        dao.insertWeather(weather)
    }

    override suspend fun deleteWeather(weather: WeatherResponse) {
        dao.delete(weather)
    }

    override suspend fun getStoredWeather(): Flow<List<WeatherResponse>> {
      return dao.getFavWeather()
    }

    override suspend fun insetAlarm(alarm: AlarmEntity) {
        alarmDao.insertAlarm(alarm)
    }

    override suspend fun deleteAlarm(alarm: AlarmEntity) {
      alarmDao.deleteAlarm(alarm)
    }

    override suspend fun getStoredAlarm(): Flow<List<AlarmEntity>> {
    return alarmDao.getAlarm()
    }
}