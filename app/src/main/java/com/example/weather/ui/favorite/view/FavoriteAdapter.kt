package com.example.weather.ui.favorite.view

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.databinding.FavoriteItemBinding
import com.example.weather.model.weather.WeatherResponse

class FavoriteAdapter (private val context: Context,private val listener:OnFavClickListener) :
    RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {
    lateinit var binding: FavoriteItemBinding
    private var cityList: List<WeatherResponse> = ArrayList<WeatherResponse>()

    fun setCityList(dayList:List<WeatherResponse>) {
        this.cityList = dayList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater =parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)as LayoutInflater
        binding= FavoriteItemBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val city = cityList[position]

        holder.binding.tvCityName.text = city.city.name
        holder.binding.ivDelete.setImageResource(R.drawable.delete)
        holder.binding.ivDelete.setOnClickListener {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val dialogView = inflater.inflate(R.layout.custom_dialog, null)

            val dialog = AlertDialog.Builder(context)
                .setView(dialogView)
                .create()

            dialogView.findViewById<Button>(R.id.btn_yes).setOnClickListener {
                // Handle positive button click
                listener.onFavClick(city)
                dialog.dismiss()
            }

            dialogView.findViewById<Button>(R.id.btn_no).setOnClickListener {
                // Handle negative button click
                dialog.dismiss()
            }

            dialog.show()
        }
//        holder.itemView.setOnClickListener{
//            val bundle = Bundle()
//            bundle.putString("date", cityList.toString()
//               // city.city.coord.lat.toString()
//            ) // Assuming date is a property of WeatherResponse
//
//            val navController = Navigation.findNavController(holder.itemView)
//            navController.navigate(R.id.homeFragment, bundle)
//        }
        holder.itemView.setOnClickListener{
            val bundle = Bundle()
            bundle.putSerializable("weatherResponse", city) // Assuming WeatherResponse implements Serializable

            val navController = Navigation.findNavController(holder.itemView)
            navController.navigate(R.id.homeFragment, bundle)
        }






    }

    override fun getItemCount(): Int {
        return cityList.size
    }

    class ViewHolder(var binding: FavoriteItemBinding): RecyclerView.ViewHolder(binding.root)

}