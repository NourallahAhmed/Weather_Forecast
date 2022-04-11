package com.example.weather.favorite.fav_view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.FavModel.FavModel
import com.example.weather.R
import com.example.weather.favorite.fav_ViewModel.OnFavClicked

class Fav_Adapter(var listener: OnFavClicked) : RecyclerView.Adapter<Fav_Adapter.Fav_Holder>() {

    lateinit var favCountries: List<FavModel>


    class Fav_Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var countryname: TextView =itemView.findViewById(R.id.countryID)
        var delete: Button =itemView.findViewById(R.id.delete)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Fav_Holder {
    return Fav_Holder(LayoutInflater.from(parent.context).inflate(R.layout.fav_row , parent , false))
    }

    override fun onBindViewHolder(holder: Fav_Holder, position: Int) {
        println("from recycler ${favCountries[position].timezone}")

        holder.itemView.setOnClickListener{
            listener.onMovieCLicked(favCountries[position].lat,favCountries[position].lon)
            println("form adapter ${favCountries[position].lat} + ${favCountries[position].lon} ")
        }
        println("name city ${favCountries[position].timezone}")
        holder.countryname.text=favCountries[position].timezone
        holder.delete.setOnClickListener{
                listener.onMovieDeleted(favCountries[position])
        }
    }

    override fun getItemCount(): Int {
        println("from recycler ${favCountries.size}")
        return favCountries.size
    }
}