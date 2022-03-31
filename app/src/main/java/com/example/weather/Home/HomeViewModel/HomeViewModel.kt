package com.example.weather.Home.HomeViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.Repo.Repo
import com.example.weather.Model.WeatherModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(var repo: Repo) :ViewModel() {


    var long:Double ?=null
    var lat:Double ?=null


    private val dataMutable =MutableLiveData<WeatherModel>()

    val dataimmutable : LiveData<WeatherModel> =dataMutable

    fun setLocation(long:Double , lat :Double){
        this.long=long
        this.lat=lat
        println( ":::::::::::::::::: Repo:   $long +  $lat" + repo)
    }



    fun getrequest() {
        viewModelScope.launch {
            println( ":::::::::::::::::: Repo:   $long +  $lat" + repo)
            var result=repo.getNetworkData(lat = lat!! ,
                lon = long!! ,
                unit = "standard",
                lang = "en")
            withContext(Dispatchers.Main){
                dataMutable.postValue(result)
                println("hhhhhhhhhh"+result.toString())
            }
        }
    }
}