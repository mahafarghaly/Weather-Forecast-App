package com.example.weather.model.repo

import com.example.weather.model.entity.AlarmEntity
import com.example.weather.model.fake.FakeWeatherLocalDataSource
import com.example.weather.model.fake.FakeWeatherRemoteDataSource
import com.example.weather.model.weather.City
import com.example.weather.model.weather.Clouds
import com.example.weather.model.weather.Coord
import com.example.weather.model.weather.Main
import com.example.weather.model.weather.Sys
import com.example.weather.model.weather.Weather
import com.example.weather.model.weather.WeatherItem
import com.example.weather.model.entity.WeatherResponse
import com.example.weather.model.weather.Wind
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test


class WeatherRepositoryTest {

    private lateinit var remoteDataSource: FakeWeatherRemoteDataSource
    private lateinit var localDataSource: FakeWeatherLocalDataSource
    private lateinit var repository: WeatherRepositoryImpl

    @Before
    fun setUp() {
        remoteDataSource = FakeWeatherRemoteDataSource()
        localDataSource = FakeWeatherLocalDataSource()
        repository = WeatherRepositoryImpl.getInstance(remoteDataSource, localDataSource)
    }

    @Test
    fun test_fetching_weather_from_remote_data_source() = runBlockingTest {
        // Given
        val lat = 0.0
        val lon = 0.0
    val apiKey ="5821a38d0f5b56ff77dcd5d5a0862647"
        val unit="metric"
        val language="en"
        // When
        val result = repository.getWeather(lat, lon, apiKey , unit, language).first()

        // Then
        assertNotNull(result)

    }

    @Test
    fun test_storing_weather_locally() = runBlockingTest {
        val lat = 0.0
        val lon = 0.0

        // When
        val weatherResponse = repository.insertWeather(lat, lon)

        // Then
        val storedWeather = repository.getStoredWeather().first()
       // assertTrue(storedWeather.contains(weatherResponse))
        assertThat(storedWeather.contains(weatherResponse), `is`(true))

    }
    @Test
    fun test_deleting_weather_locally() = runBlockingTest {
        // Given
        val weatherResponse = WeatherResponse(
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
        )
        repository.insertWeather(0.0,0.0)

        // When
        repository.deleteWeather(weatherResponse)

        // Then
        val storedWeather = repository.getStoredWeather().first()
        //assertFalse(storedWeather.contains(weatherResponse))
        assertThat(storedWeather.contains(weatherResponse), `is`(false))
    }

  @Test
  fun test_storing_alarm_locally()=runBlockingTest {
      val lat = 0.0
      val lon = 0.0
      val time=12345L

      // When
      val alarmEntity = repository.insertAlarm(lat, lon,time)

      // Then
      val storedAlarm = repository.getStoredAlarm().first()
      // assertTrue(storedWeather.contains(weatherResponse))
      assertThat(
          storedAlarm.any { it.lat == lat && it.lon == lon && it.time == time },
          `is`(true))
  }
    @Test
    fun test_deleting_alarm_locally() = runBlockingTest {
        val lat = 0.0
        val lon = 0.0
        val time = 12345L

        // Insert an alarm
        val alarmEntity = AlarmEntity(0,lat, lon, time)
        repository.insertAlarm(lat, lon, time)

        // Verify that the alarm is stored
        var storedAlarm = repository.getStoredAlarm().first()
        assertThat(storedAlarm.contains(alarmEntity), `is`(true))

        // Delete the alarm
        repository.deleteAlarm(alarmEntity)

        // Verify that the alarm is deleted
        storedAlarm = repository.getStoredAlarm().first()
        assertThat(storedAlarm.contains(alarmEntity), `is`(false))
    }

}
