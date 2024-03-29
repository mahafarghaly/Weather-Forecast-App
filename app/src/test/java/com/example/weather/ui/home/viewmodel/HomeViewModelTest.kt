package com.example.weather.ui.home.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weather.model.fake.FakeWeatherRepository
import com.example.weather.network.ApiState
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class HomeViewModelTest {

//    @get:Rule
//    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var viewModel: HomeViewModel
    private lateinit var fakeRepository: FakeWeatherRepository
    @Before
    fun setUp() {
        fakeRepository = FakeWeatherRepository()
        viewModel = HomeViewModel(fakeRepository)
    }
    @Test
    fun `getWeather should update weather state to Success when API call succeeds`() {

        val latitude = 0.0
        val longitude = 0.0

        viewModel.getWeather(latitude, longitude)

        // Assert
        val weatherState = viewModel.weather.value
        assertThat(weatherState, `is`(instanceOf(ApiState.Success::class.java)))
    }

}
