package com.example.weather.ui.favorite.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weather.model.entity.WeatherResponse
import com.example.weather.model.fake.FakeWeatherRepository
import com.example.weather.model.weather.City
import com.example.weather.model.weather.Coord
import getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class FavViewModelTest {
    @get:Rule
    val myRule=InstantTaskExecutorRule()
    lateinit var viewModel:FavViewModel
    lateinit var repo:FakeWeatherRepository
    @Before
    fun setUp(){
        repo= FakeWeatherRepository()
        viewModel= FavViewModel(repo)
    }
    @Test
    fun addFav_updatesWeatherList() {

        val latitude =40.7128
        val longitude = -74.0060

        viewModel.addFav(latitude, longitude)

        val storedWeather = viewModel.weather.getOrAwaitValue()
        assertThat(storedWeather.size, `is`(1))
        assertThat(storedWeather[0].city.coord.lat, `is`(latitude))
        assertThat(storedWeather[0].city.coord.lon, `is`(longitude))
    }
    @Test
    fun deleteFromFav_removesLocationFromWeatherList() {
        val city = "Alex"
        val country = "Egypt"
        val latitude =40.7128
        val longitude = -74.0060
        viewModel.addFav(latitude, longitude)

        viewModel.deleteFav(WeatherResponse(City(city, country, Coord(latitude, longitude)), emptyList()))

        val storedWeather = viewModel.weather.getOrAwaitValue()
        assertThat(storedWeather.isEmpty(), `is`(false))
    }

}