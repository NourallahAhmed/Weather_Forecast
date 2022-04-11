package com.example.weather.DataBase

import android.content.Context
import com.example.weather.FavDataBase.FavDAO
import com.example.weather.FavDataBase.Fav_AppDataBase
import com.example.weather.FavModel.FavModel
import com.example.weather.Model.WeatherModel

class ConcreteLocalSource(var context: Context) : Local_Source {

    private val weatherDAO:WeatherDAO?
    private val favdao: FavDAO?

//    override lateinit var allStoredData: WeatherModel

    init {
        val db1: AppDataBase? = AppDataBase.getInstance(context)
        weatherDAO = db1!!.weatherDAO()


        //fav
        val db2: Fav_AppDataBase? = Fav_AppDataBase.getInstance(context)
        favdao = db2!!.favDAO()
        /*
        * ERROR-> cant access db from main thread
        * so i made fun with courotine to access the DB
        *
        * ERROR2-> cant assign val all storeddata
        * so i change it to var
        * */
    }

    override suspend fun getAllData(longitude: Double, latitude: Double): WeatherModel {
        println("from concrete local source : long= ${longitude.toLong()} and lat= ${latitude.toLong()}")
        return weatherDAO?.getStoredData()!!
    }
//
//    fun getlocaldata(){
//        CoroutineScope(Dispatchers.IO).launch {
//            allStoredData=weatherDAO!!.allStoredDatafromDB
//        }
//    }
//    companion object{
//        private var localSource: ConcreteLocalSource? = null
//        fun getInstance(context: Context): ConcreteLocalSource{
//            if(localSource == null){
//                localSource = ConcreteLocalSource(context)
//            }
//            return localSource!!
//        }
//
//    }

    override suspend fun insertData(weatherModel: WeatherModel) {
        weatherDAO?.insertData(weatherModel)
    }

    override fun deleteData(weatherModel: WeatherModel) {
        weatherDAO?.deleteData(weatherModel) }

    override suspend fun getAllfavData(): List<FavModel> {
        return favdao?.getStoredData()!!

    }

    override suspend fun insertfavData(favModel: FavModel) {

        favdao?.insertToFav(favModel)
    }

    override suspend fun deletefavData(favModel: FavModel) {
        favdao?.deleteFromFav(favModel)
    }
}