package com.example.weather.Repo

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.weather.DataBase.Local_Source
import com.example.weather.Network.Remote_Source
import com.example.weather.Model.WeatherModel

class Repo(context: Context,localSource: Local_Source , remoteSource: Remote_Source) : Repo_Interface {


    var localSource = localSource
    var remoteSource = remoteSource

    companion object{
        private var repo: Repo? = null

        fun getInstance(context: Context,
                        localSource: Local_Source ,
                        remoteSource: Remote_Source): Repo{
            if(repo == null){
                repo = Repo(context, localSource, remoteSource )
            }
            return repo as Repo
        }

    }

    override fun getStoredData(): LiveData<WeatherModel> {
            return localSource.getStored()
    }

    override  suspend fun getNetworkData(
        lat: Double,
        lon: Double,
        unit: String,
        lang: String
        ): WeatherModel {
            print("here")
     return   remoteSource.getDataFromNetwork(lat,lon,unit,lang)
    }


    override fun insertData(weathermodel: WeatherModel) {
            localSource.insertData(weathermodel)
    }

    override fun deleteData(weathermodel: WeatherModel) {
            localSource.deleteData(weathermodel)
    }

}