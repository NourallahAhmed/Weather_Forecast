package com.example.weather.Network

import com.example.weather.Model.WeatherModel

interface Remote_Source {
    suspend fun getDataFromNetwork(lat : Double ,
                                   lon:Double ,
                                   unit:String ,
                                   lang :String): WeatherModel
}