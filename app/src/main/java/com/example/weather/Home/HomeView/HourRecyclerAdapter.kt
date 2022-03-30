package com.example.weather.Home.HomeView

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.Model.Pojo
import com.example.weather.R

class HourRecyclerAdapter (context: Context) : RecyclerView.Adapter<HourRecyclerAdapter.HourAdapter> () {
//    var week : List<Pojo> = listOf()

    class HourAdapter(itemView: View) : RecyclerView.ViewHolder(itemView){
        val hour: TextView = itemView.findViewById(R.id.hour_Id)
        val image: ImageView = itemView.findViewById(R.id.ImageHour_ID)
        val temp: TextView = itemView.findViewById(R.id.temp_hour_ID)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourAdapter {
        return HourAdapter(LayoutInflater.from(parent.context).inflate(R.layout.custom_hour_row,parent,false))
    }

    override fun onBindViewHolder(holder: HourAdapter, position: Int) {

        holder.hour.text = "12 AM"
        holder.temp.text="90 C"
//        Glide.with(context).load(week[position].image).into(holder.image);


    }

    override fun getItemCount(): Int {  return 7    } // as the week only 7 days
}