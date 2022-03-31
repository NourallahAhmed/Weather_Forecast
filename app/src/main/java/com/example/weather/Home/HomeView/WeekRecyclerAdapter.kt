package com.example.weather.Home.HomeView

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.Model.Daily

class WeekRecyclerAdapter(var context:Context): RecyclerView.Adapter<WeekRecyclerAdapter.WeekAdapter> () {
    var week : List<Daily> = listOf()

    class WeekAdapter(itemView: View) : RecyclerView.ViewHolder(itemView){
        val day: TextView = itemView.findViewById(R.id.DayWeekRowID)
        val image: ImageView = itemView.findViewById(R.id.ImageWeekRow_ID)
        val desc: TextView = itemView.findViewById(R.id.desc_Week_row_id)
        val temp: TextView = itemView.findViewById(R.id.tempWeekRow_ID)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekAdapter {
        return WeekAdapter(LayoutInflater.from(parent.context).inflate(R.layout.custom_day_row,parent,false))
    }

    override fun onBindViewHolder(holder: WeekAdapter, position: Int) {

        holder.day.text = "@2"/*week[position].dt.toString()*/ //UTC to Day
        holder.desc.text = "21"   //week[position].feelsLike.toString()
        holder.temp.text= "12" //week[position].temp.toString()
//        Glide.with(context).load(week[position].image).into(holder.image);


    }

    override fun getItemCount(): Int {  return 7    } // as the week only 7 days
}