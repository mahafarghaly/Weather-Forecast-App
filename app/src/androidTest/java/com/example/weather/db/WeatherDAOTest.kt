package com.example.weather.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.filters.MediumTest
import com.example.weather.dp.WeatherDAO
import com.example.weather.dp.WeatherDataBase
import com.example.weather.model.weather.City
import com.example.weather.model.weather.Clouds
import com.example.weather.model.weather.Coord
import com.example.weather.model.weather.Main
import com.example.weather.model.weather.Sys
import com.example.weather.model.weather.Weather
import com.example.weather.model.weather.WeatherItem
import com.example.weather.model.entity.WeatherResponse
import com.example.weather.model.weather.Wind
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@MediumTest
class WeatherDaoTest {
    private lateinit var database: WeatherDataBase
    private lateinit var weatherDao: WeatherDAO

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            WeatherDataBase::class.java
        ).allowMainThreadQueries()
            .build()
        weatherDao = database.getWeatherDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertWeather_getFavWeather_returnsInsertedWeather() = runBlocking {
        // Given
        val city = City("London", "GB", Coord(0.0, 0.0))
        val main = Main(20.0, 18.0, 15.0, 25.0, 1013, 1013, 1013, 50, 2.0)

        val weather = Weather(800, "Clear", "clear sky", "01d")

        val clouds = Clouds(0)
        val wind = Wind(10.0,33,10.0)
        val sys = Sys("ooo")
        val weatherItem = WeatherItem(123456789L, main, listOf(weather), clouds, wind, 10000, 0.5, sys, "2024-03-20 12:00:00")
        val weatherResponse = WeatherResponse(city, listOf(weatherItem))
        weatherDao.insertWeather(weatherResponse)

        // When
        val favWeatherList = weatherDao.getFavWeather().first()

        // Then
        assertThat(favWeatherList.size, `is`(1))
        assertThat(favWeatherList[0], `is`(weatherResponse))
    }

    @Test
    fun insertWeather_delete_getFavWeather_returnsEmptyList() = runBlocking {
        // Given
        val city = City("London", "GB", Coord(0.0, 0.0))
        val main = Main(20.0, 18.0, 15.0, 25.0, 1013, 1013, 1013, 50, 2.0)

        val weather = Weather(800, "Clear", "clear sky", "01d")

        val clouds = Clouds(0)
        val wind = Wind(10.0,33,10.0)
        val sys = Sys("ooo")

        val weatherItem = WeatherItem(123456789L, main, listOf(weather), clouds, wind, 10000, 0.5, sys, "2024-03-20 12:00:00")
        val weatherResponse = WeatherResponse(city, listOf(weatherItem))
        weatherDao.insertWeather(weatherResponse)

        // When
        weatherDao.delete(weatherResponse)
        val favWeatherList = weatherDao.getFavWeather().first()

        // Then
        assertThat(favWeatherList.isEmpty(), `is`(true))
    }
}
