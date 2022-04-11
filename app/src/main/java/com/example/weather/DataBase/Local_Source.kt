package com.example.weather.DataBase

import androidx.lifecycle.LiveData
import com.example.weather.FavModel.FavModel
import com.example.weather.Model.WeatherModel

interface Local_Source {
//    suspend  fun getStored(): WeatherModel

//    var allStoredData: WeatherModel

    suspend fun getAllData(longitude: Double, latitude: Double): WeatherModel
    suspend fun insertData(weatherModel: WeatherModel)
    fun deleteData(weatherModel: WeatherModel)

    //fav
    suspend fun getAllfavData(): List<FavModel>
    suspend fun insertfavData(favModel: FavModel)
    suspend fun deletefavData(favModel: FavModel)


}