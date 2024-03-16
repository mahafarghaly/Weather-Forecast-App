package com.example.weather.network

import com.example.weather.model.weather.WeatherResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("forecast")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String,
        @Query("lang") lang: String
    ): WeatherResponse
    //Response<WeatherResponse>

    object RetrofitHelper {
        private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
 val retrofitInstance= Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

        val apiService: ApiService by lazy {
            retrofitInstance.create(ApiService::class.java)
        }
    }
}