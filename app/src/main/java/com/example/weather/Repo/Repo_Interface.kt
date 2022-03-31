package com.example.weather.Repo

import androidx.lifecycle.LiveData
import com.example.weather.Model.WeatherModel

interface Repo_Interface {

    fun getStoredData(): LiveData<WeatherModel>
    suspend fun  getNetworkData(lat : Double , lon:Double , unit:String ,lang :String) :WeatherModel
    fun insertData(movie: WeatherModel)
    fun deleteData(movie: WeatherModel)
}