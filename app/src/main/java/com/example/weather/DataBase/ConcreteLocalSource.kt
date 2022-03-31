package com.example.weather.DataBase

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.weather.Model.WeatherModel

class ConcreteLocalSource(context: Context) : Local_Source {


    private var dataBase= AppDataBase.getInstance(context.applicationContext)
    private var weatherDAO: WeatherDAO? = dataBase?.weatherDAO()
    private var storedData: LiveData<WeatherModel> = weatherDAO?.getStoredData()!!

    companion object{
        private var localSource: ConcreteLocalSource? = null
        fun getInstance(context: Context): ConcreteLocalSource{
            if(localSource == null){
                localSource = ConcreteLocalSource(context)
            }
            return localSource!!
        }

    }

    override fun getStored(): LiveData<WeatherModel> {
            return  storedData
    }

    override fun insertData(weatherModel: WeatherModel) { weatherDAO?.insertData(weatherModel) }

    override fun deleteData(weatherModel: WeatherModel) { weatherDAO?.deleteData(weatherModel) }
}