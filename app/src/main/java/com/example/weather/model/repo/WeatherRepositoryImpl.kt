package com.example.weather.model.repo

import com.example.weather.dp.WeatherLocalDataSource
import com.example.weather.model.weather.WeatherItem
import com.example.weather.model.weather.WeatherResponse
import com.example.weather.network.WeatherRemoteDataSource
import com.example.weather.utilts.APIKEY
import com.example.weather.utilts.LANGUAGE
import com.example.weather.utilts.UNITS
import kotlinx.coroutines.flow.Flow

class WeatherRepositoryImpl private constructor(
private var remoteDataSource: WeatherRemoteDataSource,
private var localDataSource: WeatherLocalDataSource
):WeatherRepository {
    companion object{
        private var instance:WeatherRepositoryImpl?=null
        fun getInstance(
            weatherRemoteDataSource: WeatherRemoteDataSource,
            weatherLocalDataSource: WeatherLocalDataSource
        ):WeatherRepositoryImpl{
            return  instance?: synchronized(this){
                val temp=WeatherRepositoryImpl(
                    weatherRemoteDataSource,weatherLocalDataSource)
                instance=temp
                temp
            }
        }
    }
    override suspend fun getWeather(
        lat: Double,
        lon: Double,
        apiKey: String,
        units: String,
        lang: String
    ): WeatherResponse {
       return remoteDataSource.getWeatherOverNetwork(lat,lon,apiKey,lang,units)
    }

    override suspend fun getStoredWeather(): Flow<List<WeatherItem>> {
       return localDataSource.getStoredProducts()
    }

    override suspend fun insertWeather(late:Double,long:Double) :WeatherResponse{
        val weatherResponse = remoteDataSource.getWeatherOverNetwork(late,long, APIKEY, UNITS,
            LANGUAGE)
        localDataSource.insetWeather(weatherResponse)
        return weatherResponse
            // return localDataSource.insetWeather(weather)
    }

    override suspend fun deleteWeather(weather: WeatherResponse) {
       return localDataSource.deleteProduct(weather)
    }
}