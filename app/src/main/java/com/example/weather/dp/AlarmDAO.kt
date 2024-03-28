package com.example.weather.dp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weather.model.entity.AlarmEntity
import com.example.weather.model.entity.WeatherResponse
import kotlinx.coroutines.flow.Flow
@Dao
interface AlarmDAO {
    @Query("SELECT * FROM alarm_table")
    fun getAlarm(): Flow<List<AlarmEntity>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAlarm(alarmWeather: AlarmEntity)
    @Delete
    fun deleteAlarm(alarmWeather: AlarmEntity)
}