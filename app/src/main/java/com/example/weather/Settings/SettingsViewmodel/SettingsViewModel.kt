package com.example.weather.Settings.SettingsViewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.Model.WeatherModel
import com.example.weather.Repo.Repo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsViewModel (var repo: Repo): ViewModel() {

    var long:Double ?=null
    var lat:Double ?=null


    private val dataMutable = MutableLiveData<WeatherModel>()

    val dataimmutable : LiveData<WeatherModel> =dataMutable

    fun setLocation(long:Double , lat :Double){
        this.long=long
        this.lat=lat
    }



    fun getrequest(lang :String , unit:String) {
        viewModelScope.launch {
            var result=repo.getNetworkData(lat = lat!! ,
                lon = long!! ,
                unit = unit,
                lang = lang)
            withContext(Dispatchers.Main){
                dataMutable.postValue(result)
            }
        }
    }
}