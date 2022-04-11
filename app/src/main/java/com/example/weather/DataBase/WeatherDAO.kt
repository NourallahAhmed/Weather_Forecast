package com.example.weather.DataBase

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.weather.Model.WeatherModel

@Dao
interface WeatherDAO {
    //WHERE lat = :latitude AND lon= :longitude
    @Query("SELECT * FROM Weather ")
    suspend fun getStoredData(): WeatherModel
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(weatherModel: WeatherModel)

    //________________________FAV__________________

    //used in fav
    @Delete
    fun deleteData(weatherModel: WeatherModel)


    // todo--> insert in Fav table
    @Insert
    fun insertintofav(weatherModel: WeatherModel)
}