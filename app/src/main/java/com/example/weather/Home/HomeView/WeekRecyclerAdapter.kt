package com.example.weather.Home.HomeView

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.Model.Daily
import com.example.weather.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

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

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: WeekAdapter, position: Int) {

        println(week[position].toString())
        var weatherObj=week[position].weather[0].description

        var tempObj = week[position].temp?.day
        println("temp : " +tempObj)

        println(week[position].dt.toString())
        val formatter: DateTimeFormatter =
            DateTimeFormatter.ofPattern("MMM", Locale.ENGLISH)



//        week[position].dt?.let { getDate(it.toLong()) }
//        val aDate = "Jul 16, 2013 12:08:59 AM"
        var date = SimpleDateFormat("EE").format(week[position].dt?.times(
            1000))

      println( SimpleDateFormat("dd-mm-yyyy",Locale.ENGLISH).format(week[position].dt?.times(
            1000)))
        println("data : $date")




        holder.day.text =date//UTC to Day
        holder.desc.text =weatherObj
        holder.temp.text= week[position].temp?.day.toString()
//        Glide.with(context).load(week[position].image).into(holder.image);


    }

    override fun getItemCount(): Int {  return 7    } // as the week only 7 days


////
//    private fun getDate(time: Long): String? {
//    val format: DateFormat = SimpleDateFormat("MM/dd/YYYY hh:mm:ss a")
//    val myDate = format.parse(dateAsString)
//    System.out.println(myDate.toString())
}