package com.example.weather.model.repo

import com.example.weather.dp.WeatherLocalDataSource
import com.example.weather.model.entity.AlarmEntity
import com.example.weather.model.entity.WeatherResponse
import com.example.weather.network.WeatherRemoteDataSource
import com.example.weather.utils.APIKEY
import com.example.weather.utils.UNITS
import com.example.weather.utils.getLanguageLocale
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

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
    ): Flow<WeatherResponse> {
       return remoteDataSource.getWeatherOverNetwork(lat,lon,apiKey,lang,units)
    }

    override suspend fun getStoredWeather(): Flow<List<WeatherResponse>> {
       return localDataSource.getStoredWeather()
    }

    override suspend fun insertWeather(late:Double,long:Double) : WeatherResponse {
        val weatherResponse = remoteDataSource.getWeatherOverNetwork(late,long, APIKEY, UNITS,
            getLanguageLocale()
        )
            .first()
        localDataSource.insetWeather(weatherResponse)
        return weatherResponse
            // return localDataSource.insetWeather(weather)
    }

    override suspend fun deleteWeather(weather: WeatherResponse) {
       return localDataSource.deleteWeather(weather)
    }

    override suspend fun getStoredAlarm(): Flow<List<AlarmEntity>> {
     return localDataSource.getStoredAlarm()
    }

    override suspend fun insertAlarm(late: Double, long: Double, time: Long) {

      return localDataSource.insetAlarm(AlarmEntity(lat = late, lon = long, time = time))
    }

    override suspend fun deleteAlarm(alarm: AlarmEntity) {
   return localDataSource.deleteAlarm(alarm)
    }
}