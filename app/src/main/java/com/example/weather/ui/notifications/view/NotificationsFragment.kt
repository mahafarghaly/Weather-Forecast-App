package com.example.weather.ui.notifications.view

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.MainActivity
import com.example.weather.R
import com.example.weather.databinding.DialogTitleBinding
import com.example.weather.databinding.FragmentNotificationsBinding
import com.example.weather.databinding.SetAlarmDialogBinding
import com.example.weather.ui.home.view.HomeActivity
import com.example.weather.utils.setDate
import com.example.weather.utils.setTime
import com.example.weather.broadcast.NotificationReceiver
import com.example.weather.dp.WeatherLocalDataSourceImpl
import com.example.weather.model.entity.AlarmEntity
import com.example.weather.model.repo.WeatherRepositoryImpl
import com.example.weather.network.WeatherRemoteDataSourceImpl
import com.example.weather.ui.favorite.view.FavoriteAdapter
import com.example.weather.ui.favorite.view.MapsFragment
import com.example.weather.ui.favorite.viewmodel.FavViewModel
import com.example.weather.ui.favorite.viewmodel.FavViewModelFactory
import com.example.weather.ui.notifications.viewmodel.NotificationViewModel
import com.example.weather.ui.notifications.viewmodel.NotificationViewModelFactory
import com.example.weather.utils.openMapView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.Calendar
class NotificationsFragment : Fragment(),OnAlarmClickListener{
    private  val TAG = "NotificationsFragment"
    private var selectedDate: Calendar = Calendar.getInstance()
    lateinit var binding:FragmentNotificationsBinding
    private lateinit var materialAlertDialogBuilder: MaterialAlertDialogBuilder
    private lateinit var customAlertDialogBinding: SetAlarmDialogBinding
    lateinit var alrmViewModel:NotificationViewModel
    lateinit var alarmFactory:NotificationViewModelFactory
    lateinit var alarmAdapter: AlarmAdapter
    private lateinit var layoutManager: LinearLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        materialAlertDialogBuilder = MaterialAlertDialogBuilder(requireContext())

        super.onCreate(savedInstanceState)
        createNotificationChannel()
        val repository = WeatherRepositoryImpl.getInstance(
            WeatherRemoteDataSourceImpl.getInstance(),
            WeatherLocalDataSourceImpl.getInstance(requireContext())
        )
        alarmFactory = NotificationViewModelFactory(repository)
        alrmViewModel = ViewModelProvider(this, alarmFactory).get(NotificationViewModel::class.java)
        alrmViewModel.weather.observe(this, Observer {
                weatherState  ->
            alarmAdapter.setCityList(weatherState)
            Log.i(TAG, "list counts: ${alarmAdapter.itemCount}")
         //  selectedDate.timeInMillis=alarmAdapter.getAlarmList()[weatherState.get(0).id].time
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.floatingActionButton.setOnClickListener {
            customAlertDialogBinding =
                SetAlarmDialogBinding.inflate(LayoutInflater.from(requireContext()), null, false)
            // Launching the custom alert dialog
            launchCustomAlertDialog()
        }
        layoutManager= LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)
        binding.rvAlarm.layoutManager=layoutManager
        alarmAdapter= AlarmAdapter(requireContext(),this)
        binding.rvAlarm.adapter=alarmAdapter
    }
    private fun launchCustomAlertDialog() {
        val title = DialogTitleBinding.inflate(LayoutInflater.from(requireContext()), null, false)
        title.textTitle.text = getString(R.string.set_alarm)
        val alertDialog = materialAlertDialogBuilder.setView(customAlertDialogBinding.root)
            .setCustomTitle(title.root).setBackground(
                ResourcesCompat.getDrawable(
                    resources, R.drawable.hour_item_bacground, requireActivity().theme
                )
            ).setCancelable(false).show()
        setTimeAndDateInDialog()


        var startTime = Calendar.getInstance().timeInMillis
        val endCal = Calendar.getInstance()
        endCal.add(Calendar.DAY_OF_MONTH, 1)
        var endTime = endCal.timeInMillis

        customAlertDialogBinding.buttonSave.setOnClickListener {
            // Update the alarm with the new start time
            // updateAlarm(startTime)
            scheduleNotification(requireContext()/*,selectedDate.timeInMillis*/)
            alertDialog.dismiss()

             //Navigate to HomeActivity
            with(Intent(requireContext(), HomeActivity::class.java)) {
                startActivity(this)
            }
            // openMapView(-34.0, 151.0,selectedDate.timeInMillis)


        }


        customAlertDialogBinding.cardViewChooseStart.setOnClickListener {
            setAlarm(startTime) { currentTime ->
                startTime = currentTime
                customAlertDialogBinding.textViewStartDate.setDate(currentTime)
                customAlertDialogBinding.textViewStartTime.setTime(currentTime)
            }
        }
        customAlertDialogBinding.cardViewChooseEnd.setOnClickListener {
            setAlarm(endTime) { currentTime ->
                endTime = currentTime
                customAlertDialogBinding.textViewEndDate.setDate(currentTime)
                customAlertDialogBinding.textViewEndTime.setTime(currentTime)
            }
        }
        customAlertDialogBinding.buttonCancel.setOnClickListener {
            alertDialog.dismiss()
        }

    }
    private fun setTimeAndDateInDialog() {
        val calendar = Calendar.getInstance()
        val currentTime = calendar.timeInMillis
        customAlertDialogBinding.textViewStartDate.setDate(currentTime)
        //
        customAlertDialogBinding.textViewStartTime.setTime(currentTime)
        //
        val timeAfterOneHour = calendar.get(Calendar.HOUR_OF_DAY)
        calendar.set(Calendar.HOUR_OF_DAY, timeAfterOneHour + 2)
        customAlertDialogBinding.textViewEndDate.setDate(calendar.timeInMillis)
        //
        customAlertDialogBinding.textViewEndTime.setTime(calendar.timeInMillis)
        //
    }
    private fun setAlarm(minTime: Long, callback: (Long) -> Unit) {
        val color = ResourcesCompat.getColor(resources, R.color.purple, requireActivity().theme)
        Calendar.getInstance().apply {
            this.set(Calendar.SECOND, 0)
            this.set(Calendar.MILLISECOND, 0)
            val datePickerDialog = DatePickerDialog(
                requireContext(), R.style.DialogTheme, { _, year, month, day ->
                    this.set(Calendar.YEAR, year)
                    this.set(Calendar.MONTH, month)
                    this.set(Calendar.DAY_OF_MONTH, day)
                    val timePickerDialog = TimePickerDialog(
                        requireContext(), R.style.DialogTheme, { _, hour, minute ->
                            this.set(Calendar.HOUR_OF_DAY, hour)
                            this.set(Calendar.MINUTE, minute)
                            callback(this.timeInMillis)
                        }, this.get(Calendar.HOUR_OF_DAY), this.get(Calendar.MINUTE), false
                    )
                    timePickerDialog.show()
                    timePickerDialog.setCancelable(false)
                    timePickerDialog.getButton(TimePickerDialog.BUTTON_NEGATIVE).setTextColor(color)
                    timePickerDialog.getButton(TimePickerDialog.BUTTON_POSITIVE).setTextColor(color)
                },

                this.get(Calendar.YEAR), this.get(Calendar.MONTH), this.get(Calendar.DAY_OF_MONTH)

            )
            datePickerDialog.datePicker.minDate = minTime
            datePickerDialog.show()
            datePickerDialog.setCancelable(false)
            datePickerDialog.getButton(TimePickerDialog.BUTTON_NEGATIVE).setTextColor(color)
            datePickerDialog.getButton(TimePickerDialog.BUTTON_POSITIVE).setTextColor(color)

        }
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            val notificationManager =
                requireContext().getSystemService(NotificationManager::class.java) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    private fun scheduleNotification(context: Context/*,timeMill:Long*/) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            NOTIFICATION_PERM,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
//
//        val calendar = Calendar.getInstance()
//        calendar.add(Calendar.MINUTE, 1)

        alarmManager.set(AlarmManager.RTC_WAKEUP, selectedDate.timeInMillis, pendingIntent)
    }
    companion object {
        const val CHANNEL_ID = "CHANNEL_ID"
        const val NOTIFICATION_PERM = 1023
    }

    override fun onAlarmClick(fav: AlarmEntity?) {
        fav?.let { alrmViewModel.deletealarm(it) }
        Toast.makeText(requireContext(), " removed from alarm", Toast.LENGTH_SHORT).show()

    }
    private fun openMapView(long: Double, land: Double,time:Long) {
        val fragment = MapsFragment().apply {
            arguments = Bundle().apply {
                putDouble("long", long)
                putDouble("land", land)
                putLong("time",time)
            }
        }
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

}



