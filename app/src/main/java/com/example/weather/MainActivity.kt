package com.example.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.weather.network.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var apiService: ApiService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
      //  apiService = RetrofitHelper.apiService

        // Example coordinates for Bologna, Italy
        val lat = 44.34
        val lon = 10.99
        val apiKey = "5821a38d0f5b56ff77dcd5d5a0862647"
        val units="metric"
        val lang="en"

        // Use a coroutine to make the API call
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                val response = apiService.getWeather(lat, lon, apiKey,units,lang)
//                withContext(Dispatchers.Main) {
//                    if (response.isSuccessful) {
//                        val responseBody = response.body()
//                        // Process the response body here
//                        if (responseBody != null) {
//                            Log.d("MainActivity", "Weather data: $responseBody")
//                            // Update UI with weather data
//                        } else {
//
//                        }
//                    } else {
//                        // Handle the error
//                        Log.e("MainActivity", "Error: ${response.code()} ${response.message()}")
//                    }
//                }
//            } catch (e: Exception) {
//                // Handle the exception
//                Log.e("MainActivity", "Exception: ${e.message}")
//            }
//        }
    }
}
