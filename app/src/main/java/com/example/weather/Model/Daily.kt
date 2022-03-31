package  com.example.weather.Model

import androidx.room.Embedded
import com.google.gson.annotations.SerializedName


data class Daily (

  @SerializedName("dt"         ) var dt        : Int?               = null,
  @SerializedName("sunrise"    ) var sunrise   : Int?               = null,
  @SerializedName("sunset"     ) var sunset    : Int?               = null,
  @SerializedName("moonrise"   ) var moonrise  : Int?               = null,
  @SerializedName("moonset"    ) var moonset   : Int?               = null,
  @SerializedName("moon_phase" ) var moonPhase : Double?            = null,
  @SerializedName("temp"       ) var temp      : Temp?              = Temp(),
  @SerializedName("feels_like" ) var feelsLike : FeelsLike?         = FeelsLike(),
  @SerializedName("pressure"   ) var pressure  : Int?               = null,
  @SerializedName("humidity"   ) var humidity  : Int?               = null,
  @SerializedName("dew_point"  ) var dewPoint  : Double?            = null,
  @SerializedName("wind_speed" ) var windSpeed : Double?            = null,
  @SerializedName("wind_deg"   ) var windDeg   : Int?               = null,
  @SerializedName("wind_gust"  ) var windGust  : Double?            = null,
  @SerializedName("weather"    ) var weather   : ArrayList<Weather> = arrayListOf(),
  @SerializedName("clouds"     ) var clouds    : Int?               = null,
  @SerializedName("pop"        ) var pop       : Double?            = null,
  @SerializedName("uvi"        ) var uvi       : Double?            = null

)