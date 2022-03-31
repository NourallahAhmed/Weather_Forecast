package  com.example.weather.Model

import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName

@TypeConverters(My_Converter::class)
data class Weather (

  @SerializedName("id"          ) var id          : Int?    = null,
  @SerializedName("main"        ) var main        : String? = null,
  @SerializedName("description" ) var description : String? = null,
  @SerializedName("icon"        ) var icon        : String? = null

)