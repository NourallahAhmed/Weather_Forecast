package  com.example.weather.Model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

//@Entity(tableName = "Hourly")
data class Hourly (

  @SerializedName("dt"         )    /* @ColumnInfo(name = "dt") */          var dt         : Int?               = null,
  @SerializedName("temp"       )   /*  @ColumnInfo(name = "temp") */        var temp       : Double?            = null,
  @SerializedName("feels_like" )  /*   @ColumnInfo(name = "feels_like")*/   var feelsLike  : Double?            = null,
  @SerializedName("pressure"   )   /*  @ColumnInfo(name = "pressure")   */  var pressure   : Int?               = null,
  @SerializedName("humidity"   )    /* @ColumnInfo(name = "feels_like") */  var humidity   : Int?               = null,
  @SerializedName("dew_point"  ) /*    @ColumnInfo(name = "dew_point")*/    var dewPoint   : Double?            = null,
  @SerializedName("uvi"        )   /*  @ColumnInfo(name = "uvi")   */       var uvi        : Double?            = null,
  @SerializedName("clouds"     )   /*  @ColumnInfo(name = "clouds") */      var clouds     : Int?               = null,
  @SerializedName("visibility" )   /*  @ColumnInfo(name = "visibility") */  var visibility : Int?               = null,
  @SerializedName("wind_speed" )    /* @ColumnInfo(name = "windspeed") */   var windSpeed  : Double?            = null,
  @SerializedName("wind_deg"   )    /* @ColumnInfo(name = "winddeg")*/      var windDeg    : Int?               = null,
  @SerializedName("wind_gust"  )   /*  @ColumnInfo(name = "windgust")*/     var windGust   : Double?            = null,
  @SerializedName("weather"    )   /*  @ColumnInfo(name = "weather") */var weather    : ArrayList<Weather> = arrayListOf(),
  @SerializedName("pop"        )    /* @ColumnInfo(name = "pop")   */       var pop        : Double?               = null

)