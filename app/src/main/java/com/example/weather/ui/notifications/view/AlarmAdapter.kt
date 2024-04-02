package com.example.weather.ui.notifications.view

import android.app.AlertDialog
import android.content.Context
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.databinding.AlarmItemBinding
import com.example.weather.databinding.FavoriteItemBinding
import com.example.weather.model.entity.AlarmEntity
import com.example.weather.model.entity.WeatherResponse
import com.example.weather.ui.favorite.view.FavoriteAdapter
import com.example.weather.ui.favorite.view.OnFavClickListener
import java.sql.Date

class AlarmAdapter (private val context: Context, private val listener: OnAlarmClickListener) :
    RecyclerView.Adapter<AlarmAdapter.ViewHolder>() {
    lateinit var binding: AlarmItemBinding
    private var alarmList: List<AlarmEntity> = ArrayList<AlarmEntity>()

    fun setCityList(dayList:List<AlarmEntity>) {
        this.alarmList = dayList
        notifyDataSetChanged()
    }
    fun getAlarmList():List<AlarmEntity>{
        return alarmList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater =parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)as LayoutInflater
        binding= AlarmItemBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val alarm = alarmList[position]
        val alertTime = alarm.time
        holder.binding.tvAlarmDate.text = convertTimeInMillisToDate(context, alertTime)         //alarm.time.toString()
        holder.binding.ivDelete.setImageResource(R.drawable.delete)
        holder.binding.ivDelete.setOnClickListener {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val dialogView = inflater.inflate(R.layout.custom_dialog, null)

            val dialog = AlertDialog.Builder(context)
                .setView(dialogView)
                .create()

            dialogView.findViewById<Button>(R.id.btn_yes).setOnClickListener {
                // Handle positive button click
                listener.onAlarmClick(alarm)
                dialog.dismiss()
            }

            dialogView.findViewById<Button>(R.id.btn_no).setOnClickListener {
                // Handle negative button click
                dialog.dismiss()
            }

            dialog.show()
        }
    }

    override fun getItemCount(): Int {
        return alarmList.size
    }

    class ViewHolder(var binding: AlarmItemBinding): RecyclerView.ViewHolder(binding.root)
    fun convertTimeInMillisToDate(context: Context, timeInMillis: Long): String {
        val dateFormat = DateFormat.getDateFormat(context)
        val timeFormat = DateFormat.getTimeFormat(context)

        val date = Date(timeInMillis)

        val dateString = dateFormat.format(date)
        val timeString = timeFormat.format(date)

        return "$dateString $timeString"
    }
}