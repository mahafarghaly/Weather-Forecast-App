package com.example.weather.ui.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather.databinding.HourItemBinding
import com.example.weather.model.weather.WeatherItem
import java.text.SimpleDateFormat
import java.util.Locale

class DayHourAdapter (private val context: Context) :
    RecyclerView.Adapter<DayHourAdapter.ViewHolder>() {
    lateinit var binding:HourItemBinding
    private var dayList: List<WeatherItem> = ArrayList<WeatherItem>()

    fun setDayList(dayList: List<WeatherItem>) {
        this.dayList = dayList.take(8).drop(1)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater:LayoutInflater=parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)as LayoutInflater
        binding=HourItemBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val day = dayList[position]

       var time= day.dt_txt
            .split(" ")[1].substring(0, 5)
        holder.binding.tvHoure.text=
            convertTo12HourFormat(time)
        holder.binding.tvDTemp.text = day.main.temp_min.toInt().toString()+"°C"
                    Glide.with(context)
                .load("https://openweathermap.org/img/wn/${day.weather[0].icon}@2x.png")

                .into(binding.ivState)
    }

    override fun getItemCount(): Int {
        return dayList.size
    }

    class ViewHolder(var binding: HourItemBinding):RecyclerView.ViewHolder(binding.root)
    fun convertTo12HourFormat(time24: String): String {
        val inputFormat = SimpleDateFormat("HH", Locale.getDefault())
        val outputFormat = SimpleDateFormat("hh a", Locale.getDefault())
        val date = inputFormat.parse(time24)
        return outputFormat.format(date)
    }
    }
