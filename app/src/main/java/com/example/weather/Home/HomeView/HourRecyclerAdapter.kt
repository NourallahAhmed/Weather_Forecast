package com.example.weather.Home.HomeView

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.weather.Model.Hourly
import com.example.weather.R
import java.text.SimpleDateFormat
import java.util.*

class HourRecyclerAdapter (var context: Context) : RecyclerView.Adapter<HourRecyclerAdapter.HourAdapter> () {
    lateinit var hours : List<Hourly>

    class HourAdapter(itemView: View) : RecyclerView.ViewHolder(itemView){
        val hour: TextView = itemView.findViewById(R.id.hour_Id)
        val image: ImageView = itemView.findViewById(R.id.ImageHour_ID)
        val temp: TextView = itemView.findViewById(R.id.temp_hour_ID)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourAdapter {
        return HourAdapter(LayoutInflater.from(parent.context).inflate(R.layout.custom_hour_row,parent,false))
    }

    override fun onBindViewHolder(holder: HourAdapter, position: Int) {

        if(Home_View.LangToAdapter.equals("ar")) {
            var date = SimpleDateFormat("hh:mm a", Locale("ar")).format(hours[position].dt?.times(1000))
            holder.hour.text = date

            /*
            *
            *  var date= Date((week[position].dt!! *1000).toLong())
            *  val format = SimpleDateFormat("EE", Locale("en"))
            *  holder.day.text =format.format(date)//UTC to Day
            * */
        }
        else{
            var date = SimpleDateFormat("h:mm a", Locale("en")).format(hours[position].dt?.times(1000))
            holder.hour.text = date

        }

        holder.temp.text=hours.get(position).temp.toString()

        var weather = hours[position].weather[0]

        Glide.with(context).load("http://openweathermap.org/img/wn/${weather.icon}@2x.png").apply(
            RequestOptions().override(100, 100).error(R.drawable.ic_launcher_foreground)
        ).into(holder.image)
    }

    override fun getItemCount(): Int {  return hours.size   }
}