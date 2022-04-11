package com.example.weather.FavDataBase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.weather.FavModel.FavModel

@Dao
interface FavDAO {

    @Query("SELECT * FROM FavWeather")
    suspend fun getStoredData():List <FavModel>

    @Insert
    suspend fun insertToFav(model: FavModel)

    @Delete
    suspend fun deleteFromFav(model: FavModel)
}