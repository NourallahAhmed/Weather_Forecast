package com.example.weather.FavDataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weather.FavModel.FavModel

@Database(entities = [FavModel::class], version = 3)
abstract class Fav_AppDataBase : RoomDatabase() {

    abstract fun favDAO(): FavDAO?
    //singleton
    companion object{
        private var instance: Fav_AppDataBase? = null

        //one thread at a time to access this method
        @Synchronized
        fun getInstance(context: Context): Fav_AppDataBase?{
            return instance?: Room.databaseBuilder(
                context.applicationContext,Fav_AppDataBase::class.java,
                "FavWeather")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}