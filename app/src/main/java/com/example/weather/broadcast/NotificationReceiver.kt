package com.example.weather.broadcast

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import com.example.weather.R
import com.example.weather.dp.WeatherLocalDataSourceImpl
import com.example.weather.model.entity.WeatherResponse
import com.example.weather.model.repo.WeatherRepositoryImpl
import com.example.weather.network.ApiService
import com.example.weather.network.WeatherRemoteDataSourceImpl
import com.example.weather.ui.favorite.viewmodel.FavViewModel
import com.example.weather.ui.favorite.viewmodel.FavViewModelFactory
import com.example.weather.ui.home.view.HomeActivity
import com.example.weather.utils.APIKEY
import com.example.weather.utils.LANGUAGE
import com.example.weather.utils.UNITS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val CHANNEL_ID:String="CHANNEL_ID"
const val NOTIFICATION_PERM=1023
private lateinit var apiService: ApiService
lateinit var response: WeatherResponse
private const val TAG = "NotificationReceiver"
class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        apiService = ApiService.RetrofitHelper.apiService

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = WeatherRepositoryImpl.getInstance(
                    WeatherRemoteDataSourceImpl.getInstance(),
                    WeatherLocalDataSourceImpl.getInstance(context)
                )
//                val lat= repository.getStoredAlarm().first().get(0).lat
//                val lon=repository.getStoredAlarm().first().get(0).lon
//                Log.i(TAG, "onReceive: lat:$lat")
//                Log.i(TAG, "onReceive: lon:$lon")
                repository.getStoredAlarm().collect { alarms ->
                    var lat=0.0
                    var lon=0.0
                    alarms.forEach { alarm ->
                        lat = alarm.lat
                        lon = alarm.lon
                        Log.i(TAG, "onReceive: lat:$lat")
                        Log.i(TAG, "onReceive: lon:$lon")

                        response = apiService.getWeather(lat, lon, APIKEY, UNITS, LANGUAGE)
                        //*******************
                        withContext(Dispatchers.Main) {
                            val notificationManager =
                                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                            val notification = createNotification(context).build()
                            notificationManager.notify(NOTIFICATION_PERM, notification)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("NotificationReceiver", "Failed to fetch weather data", e)
            }
        }
    }
        // Show the notification here
//        val notificationManager =
//            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        val notification = createNotification(context).build()
//        notificationManager.notify(NOTIFICATION_PERM, notification)
    }

    private fun createNotification(context: Context): NotificationCompat.Builder {
        val intent = Intent(context, HomeActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Weather Forecast")
            .setContentText("${ response.city.name},${ response.list.get(0).weather.get(0).description}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
    }

