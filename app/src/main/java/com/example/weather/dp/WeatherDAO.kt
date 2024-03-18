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
    @Query("SELECT json_extract(list, '\$.dt') AS dt, json_extract(list, '\$.main') AS main, json_extract(list, '\$.weather') AS weather, json_extract(list, '\$.clouds') AS clouds, json_extract(list, '\$.wind') AS wind, json_extract(list, '\$.visibility') AS visibility, json_extract(list, '\$.pop') AS pop, json_extract(list, '\$.sys') AS sys, json_extract(list, '\$.dt_txt') AS dt_txt FROM favorite_table")
    fun getFavWeather(): Flow<List<WeatherItem>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWeather(favWeather:WeatherResponse)
    @Delete
    fun delete(fvWeather: WeatherResponse)
}