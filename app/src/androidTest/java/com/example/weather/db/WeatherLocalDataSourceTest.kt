package com.example.weather.db

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.MediumTest
import com.example.weather.dp.WeatherDataBase
import com.example.weather.dp.WeatherLocalDataSourceImpl
import com.example.weather.model.weather.City
import com.example.weather.model.weather.Clouds
import com.example.weather.model.weather.Coord
import com.example.weather.model.weather.Main
import com.example.weather.model.weather.Sys
import com.example.weather.model.weather.Weather
import com.example.weather.model.weather.WeatherItem
import com.example.weather.model.weather.WeatherResponse
import com.example.weather.model.weather.Wind
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@MediumTest
class WeatherLocalDataSourceTest
 {
    private lateinit var context: Context
    private lateinit var database: WeatherDataBase
    private lateinit var dataSource: WeatherLocalDataSourceImpl

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        database = Room.inMemoryDatabaseBuilder(context, WeatherDataBase::class.java)
            .allowMainThreadQueries()
            .build()
        dataSource = WeatherLocalDataSourceImpl.getInstance(context)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertWeather_getStoredProducts_returnsInsertedWeather() = runBlocking {
        // Given
        val city = City("London", "GB", Coord(0.0, 0.0))
        val main = Main(20.0, 18.0, 15.0, 25.0, 1013, 1013, 1013, 50, 2.0)

        val weather = Weather(800, "Clear", "clear sky", "01d")

        val clouds = Clouds(0)
        val wind = Wind(10.0,33,10.0)
        val sys = Sys("ooo")
        val weatherItem = WeatherItem(123456789L, main, listOf(weather), clouds, wind, 10000, 0.5, sys, "2024-03-20 12:00:00")
        val weatherResponse = WeatherResponse(city, listOf(weatherItem))

        // When
        dataSource.insetWeather(weatherResponse)
        val storedWeatherList = dataSource.getStoredWeather().first()

        // Then
        assertThat(storedWeatherList.size, `is`(1))
        assertThat(storedWeatherList[0], `is`(weatherResponse))
    }

    @Test
    fun insertWeather_deleteProduct_getStoredProducts_returnsEmptyList() = runBlockingTest {
        // Given
        val city = City("London", "GB", Coord(0.0, 0.0))
        val main = Main(20.0, 18.0, 15.0, 25.0, 1013, 1013, 1013, 50, 2.0)
        val weather = Weather(800, "Clear", "clear sky", "01d")
        val clouds = Clouds(0)
        val wind = Wind(10.0,33,10.0)
        val sys = Sys("ooo")
        val weatherItem = WeatherItem(123456789L, main, listOf(weather), clouds, wind, 10000, 0.5, sys, "2024-03-20 12:00:00")
        val weatherResponse = WeatherResponse(city, listOf(weatherItem))

        // When
        dataSource.insetWeather(weatherResponse)
        dataSource.deleteProduct(weatherResponse)
        val storedWeatherList = dataSource.getStoredWeather().first()

        // Then
        assertThat(storedWeatherList.isEmpty(), `is`(true))
    }
}
