package com.example.weather.Model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type



object My_Converter {


//    @TypeConverter
//    fun fromStringM(value: String?): ArrayList<Minutely> {
//        val listType: Type = object : TypeToken<ArrayList<Minutely?>?>() {}.getType()
//        return Gson().fromJson(value, listType)
//    }

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
      fun fromStringA(value: String?): ArrayList<Alerts>? {
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


    @TypeConverter
    fun fromCurrentToString(current: Current) = Gson().toJson(current)
    @TypeConverter
    fun fromStringToCurrent(stringCurrent : String) = Gson().fromJson(stringCurrent, Current::class.java)

    @TypeConverter
    fun fromDailyListToString(daily: List<Daily>) = Gson().toJson(daily)
    @TypeConverter
    fun fromStringToDailyList(stringDaily : String) = Gson().fromJson(stringDaily, Array<Daily>::class.java).toList()

    @TypeConverter
    fun fromHourlyListToString(hourly: List<Hourly>) = Gson().toJson(hourly)
    @TypeConverter
    fun fromStringToHourlyList(stringHourly : String) = Gson().fromJson(stringHourly, Array<Hourly>::class.java).toList()
//@TypeConverter
//fun minutelyListToString(minutelyList:  ArrayList<Minutely>?): String? {
//    if (minutelyList == null) {
//        return null
//    }
//    val gson = Gson()
//    val type: Type = object : TypeToken< ArrayList<Minutely>>(){}.type
//    return gson.toJson(minutelyList, type)
//}
//
//    @TypeConverter
//    fun minutelyStringToList(minutelyString: String?):  ArrayList<Minutely>? {
//        if (minutelyString == null) {
//            return java.util.ArrayList()
//        }
//        val gson = Gson()
//        val type: Type = object : TypeToken< ArrayList<Minutely>>() {}.type
//        return gson.fromJson(minutelyString, type)
//    }
//
//
//    @TypeConverter
//    fun hourlyListToString(hourlyList:  ArrayList<Hourly>?): String? {
//        if (hourlyList == null) {
//            return null
//        }
//        val gson = Gson()
//        val type: Type = object : TypeToken< ArrayList<Hourly>>(){}.type
//        return gson.toJson(hourlyList, type)
//    }
//
//    @TypeConverter
//    fun hourlyStringToList(hourlyString: String?):  ArrayList<Hourly>? {
//        if (hourlyString == null) {
//            return java.util.ArrayList()
//        }
//        val gson = Gson()
//        val type: Type = object : TypeToken< ArrayList<Hourly>>() {}.type
//        return gson.fromJson(hourlyString, type)
//    }
//
//
//
//    @TypeConverter
//    fun alertsListToString(alertList:  ArrayList<Alerts>?): String? {
//        if (alertList == null) {
//            return null
//        }
//        val gson = Gson()
//        val type: Type = object : TypeToken< ArrayList<Alerts>>(){}.type
//        return gson.toJson(alertList, type)
//    }
//
//    @TypeConverter
//    fun alertStringToList(alertString: String?):  ArrayList<Alerts>? {
//        if (alertString == null) {
//            return java.util.ArrayList()
//        }
//        val gson = Gson()
//        val type: Type = object : TypeToken< ArrayList<Alerts>>() {}.type
//        return gson.fromJson(alertString, type)
//    }
//    @TypeConverter
//    fun dailyListToString(dailyList:  ArrayList<Daily>?): String? {
//        if (dailyList == null) {
//            return null
//        }
//        val gson = Gson()
//        val type: Type = object : TypeToken< ArrayList<Daily>>(){}.type
//        return gson.toJson(dailyList, type)
//    }
//
//    @TypeConverter
//    fun dailyStringToList(dailyString: String?):  ArrayList<Daily>? {
//        if (dailyString == null) {
//            return java.util.ArrayList()
//        }
//        val gson = Gson()
//        val type: Type = object : TypeToken< ArrayList<Daily>>() {}.type
//        return gson.fromJson(dailyString, type)
//    }
//
//    @TypeConverter
//    fun weatherListToString(weatherList: ArrayList<Weather>?): String? {
//        if (weatherList == null) {
//            return null
//        }
//        val gson = Gson()
//        val type: Type = object : TypeToken<ArrayList<Weather>>(){}.type
//        return gson.toJson(weatherList, type)
//    }
//
//    @TypeConverter
//    fun weatherStringToList(weatherString: String?): ArrayList<Weather>? {
//        if (weatherString == null) {
//            return java.util.ArrayList()
//        }
//        val gson = Gson()
//        val type: Type = object : TypeToken<ArrayList<Weather>>() {}.type
//        return gson.fromJson(weatherString, type)
//    }


}