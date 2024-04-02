package com.example.weather.ui.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.weather.databinding.DayItemBinding
import com.example.weather.databinding.HourItemBinding
import com.example.weather.model.weather.WeatherItem
import com.example.weather.utils.getWeatherIconResourceId
import com.example.weather.utils.units.setTemp
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.roundToInt

class DaysAdapter(private val context: Context) :
    RecyclerView.Adapter<DaysAdapter.ViewHolder>() {
    lateinit var binding: DayItemBinding
    private var dayList: List<WeatherItem> = ArrayList<WeatherItem>()

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

        val dayName = convertToDayName(day.dt_txt.substring(0, 10))
        holder.binding.tvDayName.text = dayName
        holder.binding.tvWeatherState.text = day.weather[0].description
        holder.binding.tvTempDay.setTemp(
            day.main.temp_min.roundToInt(),
            holder.itemView.context
        )

        val icon= getWeatherIconResourceId(day.weather[0].icon)
        Glide.with(  holder.itemView.context)
            .load(icon)
            .apply(
                RequestOptions()

            )
            .into(binding.ivDayState)
    }

    override fun getItemCount(): Int {
        return dayList.size
    }

    class ViewHolder(var binding: DayItemBinding): RecyclerView.ViewHolder(binding.root)

    fun convertToDayName(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = inputFormat.parse(dateString) ?: return ""
        val outputFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        return outputFormat.format(date)
    }

}
