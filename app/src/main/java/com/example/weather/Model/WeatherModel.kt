package com.example.weather.Model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Weather", primaryKeys = ["lat","lon"])
data class WeatherModel (

  @SerializedName("lat")
  @ColumnInfo(name = "lat")
  @NonNull
  var lat : Double,

  @SerializedName("lon") @ColumnInfo(name = "lon")
  var lon : Double,

  @SerializedName("timezone") @ColumnInfo(name = "timezone")
  var timezone : String?= null,

  @SerializedName("timezone_offset" ) @ColumnInfo(name = "timezone_offset")
  var timezoneOffset : Int?                = null,

  @Embedded
  @SerializedName("current"         )
  var current        : Current?            = Current(),

  @SerializedName("minutely"        ) @ColumnInfo(name = "minutely")
  var minutely       : ArrayList<Minutely> = arrayListOf(),

  @SerializedName("hourly"          ) @ColumnInfo(name = "hourly")

  var hourly         : ArrayList<Hourly>   = arrayListOf(),

  @SerializedName("daily"           )  @ColumnInfo(name = "daily")
  var daily          : ArrayList<Daily>    = arrayListOf(),

  @SerializedName("alerts"          ) @ColumnInfo(name = "alerts")
  var alerts         : ArrayList<Alerts>   = arrayListOf()

)