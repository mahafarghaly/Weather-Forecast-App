package com.example.weather

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.weather.broadcast.NOTIFICATION_PERM
import com.example.weather.databinding.ActivityMainBinding
import com.example.weather.broadcast.NotificationReceiver
import com.example.weather.ui.home.view.HomeActivity
import com.example.weather.ui.notifications.view.NotificationsFragment.Companion.CHANNEL_ID
import com.example.weather.utils.changeLanguageLocaleTo
import com.example.weather.utils.openMapView
import com.mad.iti.weather.sharedPreferences.SettingSharedPreferences
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private  val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createNotificationChannel()

//        binding.oneTimebtn.setOnClickListener {
//            scheduleNotification(applicationContext)
//        }

        binding.buttonSave.setOnClickListener {
            if(binding.radioGps.isChecked){
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            }else{
             openMapView(this.supportFragmentManager,"setup",30.0,34.0)
         binding.cardViewSetup.visibility= View.GONE
            }


        }

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            val notificationManager =
                getSystemService(NotificationManager::class.java) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun scheduleNotification(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            NOTIFICATION_PERM,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MINUTE, 1)

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    companion object {
        const val CHANNEL_ID = "CHANNEL_ID"
        const val NOTIFICATION_PERM = 1023
    }
}
