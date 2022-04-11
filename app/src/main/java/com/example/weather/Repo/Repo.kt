package com.example.weather.Repo

import android.content.Context
import com.example.weather.DataBase.Local_Source
import com.example.weather.FavModel.FavModel
import com.example.weather.Network.Remote_Source
import com.example.weather.Model.WeatherModel

class Repo(var context: Context,var localSource: Local_Source , var remoteSource: Remote_Source) : Repo_Interface
{

    companion object{
        private var repo: Repo? = null

        fun getInstance(context: Context,
                        localSource: Local_Source ,
                        remoteSource: Remote_Source): Repo{
            return repo?:Repo(
                context,localSource,remoteSource)
        }

    }

    override suspend fun getStoredData(longitude: Double, latitude: Double): WeatherModel {
//            return localSource.allStoredData
        return localSource.getAllData(longitude,latitude)
    }

    override  suspend fun getNetworkData(lat: Double, lon: Double, unit: String, lang: String): WeatherModel {
        println("enter the get networkdata")
         return   remoteSource.getDataFromNetwork(lat,lon,unit,lang,"minutely")
    }


    override suspend fun insertData(weathermodel: WeatherModel) {
            localSource.insertData(weathermodel)
    }

    override suspend fun insertToFav(model: FavModel) {
        println("in insert Fav repo" )

        localSource.insertfavData(model)
    }

    override suspend fun getFavData() :List<FavModel> {

        println("from repo ${localSource.getAllfavData()}")
        return  localSource.getAllfavData()
    }

    override fun deleteData(weathermodel: WeatherModel) {
            localSource.deleteData(weathermodel)
    }

    override suspend fun deleteFav(model: FavModel) {
        localSource.deletefavData(model)
    }


}