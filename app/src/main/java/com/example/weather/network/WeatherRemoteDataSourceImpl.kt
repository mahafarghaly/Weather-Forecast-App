package com.example.weather.network

import com.example.weather.model.weather.WeatherItem
import com.example.weather.model.weather.WeatherResponse
import com.example.weather.utils.getLanguageLocale
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRemoteDataSourceImpl private constructor(): WeatherRemoteDataSource {
    private val weatherService:ApiService by lazy {
        ApiService.RetrofitHelper.retrofitInstance.create(ApiService::class.java)

    }

    override suspend fun getWeatherOverNetwork(
        lat: Double,
        lon: Double,
        apiKey: String,
        lang: String,
        units: String
    ): Flow<WeatherResponse> {
        val response=weatherService.getWeather(lat,lon,apiKey,units,getLanguageLocale())
        return flow {
          emit(response)
        }
    }

    companion object{
        private var instance:WeatherRemoteDataSourceImpl?=null
        fun getInstance():WeatherRemoteDataSourceImpl{
            return instance?: synchronized(this){
                val temp=WeatherRemoteDataSourceImpl()
                instance= temp
                temp
            }
        }
    }
}