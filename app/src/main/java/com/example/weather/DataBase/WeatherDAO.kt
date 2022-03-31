package com.example.weather.DataBase

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.weather.Model.WeatherModel

@Dao
interface WeatherDAO {
    @Query("SELECT * FROM Weather")
    fun getStoredData(): LiveData<WeatherModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertData(weatherModel: WeatherModel)


    //each time i will insert i will delete the previous data
    @Delete
    fun deleteData(weatherModel: WeatherModel)
}