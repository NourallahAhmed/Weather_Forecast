package com.example.weather.Model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type



object My_Converter {

    @TypeConverter
    fun fromString(value: String?): ArrayList<String> {
        val listType: Type = object : TypeToken<ArrayList<String?>?>() {}.getType()
        return Gson().fromJson(value, listType)
    }
    @TypeConverter
    fun fromArrayList(list: ArrayList<String?>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromStringM(value: String?): ArrayList<Minutely> {
        val listType: Type = object : TypeToken<ArrayList<Minutely?>?>() {}.getType()
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayListM(list: ArrayList<Minutely?>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromStringH(value: String?): ArrayList<Hourly> {
        val listType: Type = object : TypeToken<ArrayList<Hourly?>?>() {}.getType()
        return Gson().fromJson(value, listType)
    }
    @TypeConverter
    fun fromArrayListH(list: ArrayList<Hourly?>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromStringD(value: String?): ArrayList<Daily> {
        val listType: Type = object : TypeToken<ArrayList<Daily?>?>() {}.getType()
        return Gson().fromJson(value, listType)
    }
    @TypeConverter
    fun fromArrayListD(list: ArrayList<Daily?>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromStringA(value: String?): ArrayList<Alerts> {
        val listType: Type = object : TypeToken<ArrayList<Alerts?>?>() {}.getType()
        return Gson().fromJson(value, listType)
    }
    @TypeConverter
    fun fromArrayListA(list: ArrayList<Alerts?>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }


    @TypeConverter
    fun fromStringW(value: String?): ArrayList<Weather> {
        val listType: Type = object : TypeToken<ArrayList<Weather?>?>() {}.getType()
        return Gson().fromJson(value, listType)
    }
    @TypeConverter
    fun fromArrayListW(list: ArrayList<Weather?>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }

}