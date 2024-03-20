package com.example.weather.dp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weather.model.weather.WeatherItem
import com.example.weather.model.weather.WeatherResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDAO {
    @Query("SELECT * FROM favorite_table")
    fun getFavWeather(): Flow<List<WeatherResponse>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWeather(favWeather:WeatherResponse)
    @Delete
    fun delete(fvWeather: WeatherResponse)
}