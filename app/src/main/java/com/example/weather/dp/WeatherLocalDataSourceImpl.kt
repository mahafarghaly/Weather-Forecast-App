package com.example.weather.dp

import android.content.Context
import com.example.weather.model.weather.WeatherItem
import com.example.weather.model.weather.WeatherResponse
import kotlinx.coroutines.flow.Flow

class WeatherLocalDataSourceImpl (context: Context):WeatherLocalDataSource  {
    private  val dao:WeatherDAO by lazy {
        val db:WeatherDataBase=WeatherDataBase.getInstance(context)
        db.getWeatherDao()
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

    override suspend fun deleteProduct(weather: WeatherResponse) {
        dao.delete(weather)
    }

    override suspend fun getStoredWeather(): Flow<List<WeatherResponse>> {
      return dao.getFavWeather()
    }
}