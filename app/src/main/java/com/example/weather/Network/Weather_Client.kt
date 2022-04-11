package com.example.weather.Network

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import com.example.weather.Model.WeatherModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Weather_Client private constructor(): Remote_Source {
    private val context: Context? = null
    lateinit var myList : List<WeatherModel>

    //singleton
    companion object{

        @SuppressLint("StaticFieldLeak")
        var weatherClient : Weather_Client ?= null

        fun getinstance() : Weather_Client?{
            if(weatherClient==null){
                weatherClient= Weather_Client()
            }
            return weatherClient
        }


    }
    override suspend fun getDataFromNetwork(lat: Double, lon: Double, unit: String, lang: String, exclude :String):WeatherModel {
        //implement movie service
        var apiService = RetrofitHelper.Get_Retrofit()!!.create(WeatherService::class.java)
        var appid="00cc0edd6a289076e66954faceaf9259"
        val response=  apiService.getDataFromApi(lat.toString() , lon.toString() ,appid, unit, lang , exclude)
        println("from response $response")
        return response



    }
}