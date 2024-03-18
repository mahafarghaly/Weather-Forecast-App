package com.example.weather.ui.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather.databinding.DayItemBinding
import com.example.weather.databinding.HourItemBinding
import com.example.weather.model.weather.WeatherItem
import java.text.SimpleDateFormat
import java.util.Locale

class DaysAdapter(private val context: Context) :
    RecyclerView.Adapter<DaysAdapter.ViewHolder>() {
    lateinit var binding: DayItemBinding
    private var dayList: List<WeatherItem> = ArrayList<WeatherItem>()

//    fun setDayList(dayList: List<WeatherItem>) {
//        this.dayList = dayList.drop(8)
//        notifyDataSetChanged()
//    }
fun setDayList(dayList: List<WeatherItem>) {
    this.dayList = getFirstItemOfDay(dayList).drop(1)
    notifyDataSetChanged()
}


    private fun getFirstItemOfDay(dayList: List<WeatherItem>): List<WeatherItem> {
        val map = mutableMapOf<String, WeatherItem>()
        for (day in dayList) {
            val date = day.dt_txt.substring(0, 10)
            if (!map.containsKey(date)) {
                map[date] = day
            }
        }
        return map.values.toList()
    }
    private fun getOtherDays(dayList: List<WeatherItem>): List<WeatherItem> {
        val firstDay = dayList.firstOrNull()?.dt_txt?.substring(0, 10) ?: ""
        return dayList.filter { it.dt_txt.substring(0, 10) != firstDay }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater =parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)as LayoutInflater
        binding= DayItemBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val day = dayList[position]

//        var time= day.dt_txt
//            .split(" ")[1].substring(0, 5)
//        holder.binding.tvDayName.text=day.dt_txt.substring(0, 10)
        val dayName = convertToDayName(day.dt_txt.substring(0, 10))
        holder.binding.tvDayName.text = dayName
        holder.binding.tvWeatherState.text = day.weather[0].description
            //day.main.temp_min.toInt().toString()+"°C"
        Glide.with(context)
            .load("https://openweathermap.org/img/wn/${day.weather[0].icon}@2x.png")

            .into(binding.ivDayState)
    }

    override fun getItemCount(): Int {
        return dayList.size
    }

    class ViewHolder(var binding: DayItemBinding): RecyclerView.ViewHolder(binding.root)
//    fun convertTo12HourFormat(time24: String): String {
//        val inputFormat = SimpleDateFormat("HH", Locale.getDefault())
//        val outputFormat = SimpleDateFormat("hh a", Locale.getDefault())
//        val date = inputFormat.parse(time24)
//        return outputFormat.format(date)
//    }

    fun convertToDayName(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = inputFormat.parse(dateString) ?: return ""
        val outputFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        return outputFormat.format(date)
    }

}
