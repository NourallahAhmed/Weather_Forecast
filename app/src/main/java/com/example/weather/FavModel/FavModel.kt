package com.example.weather.FavModel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "FavWeather")
data class FavModel(
    @PrimaryKey
    @ColumnInfo(name = "lat")
    var lat : Double,
    @ColumnInfo(name = "lon")
    var lon : Double,
    @ColumnInfo(name = "timezone")
    var timezone : String?= null)
