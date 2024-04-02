package com.example.weather.broadcast

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDeepLinkBuilder
import com.example.weather.R
import com.example.weather.dp.WeatherLocalDataSourceImpl
import com.example.weather.model.entity.AlarmEntity
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val CHANNEL_ID: String = "CHANNEL_ID"
const val NOTIFICATION_PERM = 1023
private lateinit var apiService: ApiService
private const val TAG = "NotificationReceiver"
private var late: Double? = 0.0
private var long: Double? = 0.0
private var id: Int? = 0

class NotificationReceiver : BroadcastReceiver() {
    @SuppressLint("SuspiciousIndentation")
    override fun onReceive(context: Context, intent: Intent) {
        apiService = ApiService.RetrofitHelper.apiService
        val alarmTime = intent.getLongExtra("alarm_time", 0)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = WeatherRepositoryImpl.getInstance(
                    WeatherRemoteDataSourceImpl.getInstance(),
                    WeatherLocalDataSourceImpl.getInstance(context)
                )
                late = repository.getStoredAlarm().first().get(0).lat
                long = repository.getStoredAlarm().first().get(0).lon
                id = repository.getStoredAlarm().first().get(0).id
                try {
                    val response =
                        apiService.getWeather(late!!, long!!, APIKEY, UNITS, LANGUAGE)
                    val notificationManager =
                        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    val notification = createNotification(context, response).build()
                    //val notification = createCustomNotification(context, response).build()
                    notificationManager.notify(NOTIFICATION_PERM, notification)
                    repository.deleteAlarm(AlarmEntity(id!!, late!!, long!!, alarmTime))
                } catch (e: Exception) {
                    Log.e("NotificationReceiver", "Failed to fetch weather data", e)
                }
            } catch (e: Exception) {
                Log.e("NotificationReceiver", "Failed to fetch weather data", e)
            }
        }
    }

    private fun createNotification(
        context: Context,
        response: WeatherResponse
    ): NotificationCompat.Builder {
        val intent = Intent(context, HomeActivity::class.java)
//            .apply {
//            putExtra("lat", late)
//            putExtra("long", long)
//        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Weather Forecast")
            .setContentText("${response.city.name},${response.list.get(0).weather.get(0).description}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
    }

    @SuppressLint("RemoteViewLayout")
    private fun createCustomNotification(
        context: Context,
        response: WeatherResponse
    ): NotificationCompat.Builder {
        val notificationLayout = RemoteViews(context.packageName, R.layout.custom_notification)
        notificationLayout.setTextViewText(R.id.tv_title, "Custom Weather Forecast")
        notificationLayout.setTextViewText(
            R.id.tv_contentent,
            "${response.city.name}, ${response.list[0].weather[0].description}"
        )
        val intent = Intent(context, HomeActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.notifications)
            .setContentTitle("Weather Forecast")
            .setContentText("${response.city.name},${response.list.get(0).weather.get(0).description}")
            .setCustomContentView(notificationLayout)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

    }

}
