package com.example.weather.DataBase

import android.content.Context
import androidx.room.*
import com.example.weather.Model.My_Converter
import com.example.weather.Model.WeatherModel

@Database(entities = [WeatherModel::class], version = 13)
@TypeConverters(My_Converter::class)
 abstract class AppDataBase : RoomDatabase() {

    abstract fun weatherDAO(): WeatherDAO
    //singleton
    companion object{
        private var instance: AppDataBase? = null

        //one thread at a time to access this method
        @Synchronized
        fun getInstance(context: Context): AppDataBase?{
            return instance?:Room.databaseBuilder(
                context.applicationContext,AppDataBase::class.java,
                "Weather")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}