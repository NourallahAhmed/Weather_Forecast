package com.example.weather.Home.HomeView

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.Model.Hourly
import com.example.weather.R

class HourRecyclerAdapter (context: Context) : RecyclerView.Adapter<HourRecyclerAdapter.HourAdapter> () {
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

        holder.hour.text = hours.get(position).dt.toString()
        holder.temp.text=hours.get(position).temp.toString()

        println( "hourAdapter"+ hours.get(position).dt.toString() + hours.get(position).temp.toString())
//        Glide.with(context).load(week[position].image).into(holder.image);


    }

    override fun getItemCount(): Int {  return hours.size   } // as the week only 7 days
}