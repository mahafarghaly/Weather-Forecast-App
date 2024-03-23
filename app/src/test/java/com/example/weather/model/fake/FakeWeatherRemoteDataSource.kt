package com.example.weather.model.fake

import com.example.weather.model.weather.City
import com.example.weather.model.weather.Clouds
import com.example.weather.model.weather.Coord
import com.example.weather.model.weather.Main
import com.example.weather.model.weather.Sys
import com.example.weather.model.weather.Weather
import com.example.weather.model.weather.WeatherItem
import com.example.weather.model.weather.WeatherResponse
import com.example.weather.model.weather.Wind
import com.example.weather.network.WeatherRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeWeatherRemoteDataSource  : WeatherRemoteDataSource {
    override suspend fun getWeatherOverNetwork(lat: Double, lon: Double, apiKey: String, units: String, lang: String): Flow<WeatherResponse> {
        return flow {
            emit(WeatherResponse(
                city = City("Alex", "EG", Coord(40.7128, -74.0060)),
                list = listOf(
                    WeatherItem(
                        dt = 1,
                        main = Main(10.0, 20.0, 5.0, 15.0, 1000, 1000, 1000, 50, 0.0),
                        weather = listOf(Weather(1, "Clear", "clear sky", "01d")),
                        clouds = Clouds(0),
                        wind = Wind(10.0,7 ,180.0),
                        visibility = 10000,
                        pop = 0.0,
                        sys = Sys("US"),
                        dt_txt = "3-23-2024 04.00"
                    )
                )
            ))
        }
    }
}