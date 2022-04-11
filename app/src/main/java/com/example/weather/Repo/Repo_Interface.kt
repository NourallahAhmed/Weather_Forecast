package com.example.weather.Repo

import androidx.lifecycle.LiveData
import com.example.weather.FavModel.FavModel
import com.example.weather.Model.WeatherModel

interface Repo_Interface {

    suspend fun getStoredData(longitude: Double, latitude: Double): WeatherModel
    suspend fun  getNetworkData(lat : Double , lon:Double , unit:String ,lang :String) :WeatherModel
    suspend fun insertData(movie: WeatherModel)
    suspend fun insertToFav(model: FavModel)
    suspend fun getFavData(): List<FavModel>
    fun deleteData(movie: WeatherModel)
    suspend fun deleteFav(model: FavModel)
}