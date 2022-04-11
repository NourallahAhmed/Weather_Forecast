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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.weather.Model.Daily
import com.example.weather.R
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class WeekRecyclerAdapter(var context: Context, var unit: String?): RecyclerView.Adapter<WeekRecyclerAdapter.WeekAdapter> () {
    public var week : List<Daily> = listOf()

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
        var weatherObj=week[position].weather[0].description
        var tempObj = week[position].temp?.day
//        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM", Locale.ENGLISH)

        if(Home_View.LangToAdapter == "en") {
            var date= Date((week[position].dt!! *1000).toLong())

            val format = SimpleDateFormat("EE", Locale("en"))
            holder.day.text =format.format(date)//UTC to Day
        }
        else{
            var date= Date((week[position].dt!! *1000).toLong())
            val format = SimpleDateFormat("EE", Locale("ar"))
            holder.day.text =format.format(date)//UTC to Day
        }

        var weather = week[position].weather[0]

        holder.desc.text =weatherObj

//        holder.temp.text= week[position].temp?.day.toString()
        holder.temp.text= week[position].temp?.max.toString()+"/"+week[position].temp?.min.toString() + unit

        var iconurl = "http://openweathermap.org/img/wn/${weather.icon}@2x.png";

        Glide.with(holder.itemView.context).load(iconurl).apply(
            RequestOptions().override(100, 100).placeholder(R.drawable.ic_launcher_background)
            ).dontAnimate().into(holder.image)

    }

    override fun getItemCount(): Int {  return 7    } // as the week only 7 days

//    fun setWeek(update :  List<Daily>){
//        week=update
//    }
//

//    private fun getDate(time: Long): String? {
//    val format: DateFormat = SimpleDateFormat("MM/dd/YYYY hh:mm:ss a")
//    val myDate = format.parse(dateAsString)
//    System.out.println(myDate.toString())
}