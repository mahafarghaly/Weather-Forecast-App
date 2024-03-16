package com.example.weather.model.repo

import com.example.weather.model.weather.WeatherItem
import com.example.weather.model.weather.WeatherResponse
import com.example.weather.network.WeatherRemoteDataSource

class WeatherRepositoryImpl private constructor(
private var remoteDataSource: WeatherRemoteDataSource
):WeatherRepository {
    companion object{
        private var instance:WeatherRepositoryImpl?=null
        fun getInstance(
            weatherRemoteDataSource: WeatherRemoteDataSource
            //productLocalDataSource:  ProductsLocalDataSource
        ):WeatherRepositoryImpl{
            return  instance?: synchronized(this){
                val temp=WeatherRepositoryImpl(
                    weatherRemoteDataSource)
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
}