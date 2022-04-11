package com.example.weather.Network

import androidx.lifecycle.LiveData
import com.example.weather.Model.WeatherModel
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherService {

    //https://api.openweathermap.org/data/2.5/onecall?lat={lat}&lon={lon}&appid=00cc0edd6a289076e66954faceaf9259
//https://api.openweathermap.org/data/2.5/onecall?lat=29.9415396&lon=31.2216008&appid=00cc0edd6a289076e66954faceaf9259
    /*
    * use the @query and { }  to customize the output
    * */

//    @GET("data/2.5/onecall?lat={lat}&lon={lon}&appid=00cc0edd6a289076e66954faceaf9259&units={unit}&lang={lang}")
    @GET("data/2.5/onecall?")
   suspend fun getDataFromApi(@Query("lat") lat :String,
                       @Query("lon") lon :String,
                       @Query("appid") ap:String,
                       @Query("units") unit :String,
                       @Query("lang") lang :String,
                              @Query("exclude") exclude :String,
): WeatherModel

}