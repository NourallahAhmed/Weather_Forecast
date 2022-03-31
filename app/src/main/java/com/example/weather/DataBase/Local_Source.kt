package com.example.weather.DataBase

import androidx.lifecycle.LiveData
import com.example.weather.Model.WeatherModel

interface Local_Source {
    fun getStored(): LiveData<WeatherModel>

    fun insertData(weatherModel: WeatherModel)

    fun deleteData(weatherModel: WeatherModel)
}