package  com.example.weatherapp.models.responseDataModel

import com.google.gson.annotations.SerializedName


data class Minutely (

  @SerializedName("dt"            ) var dt            : Int? = null,
  @SerializedName("precipitation" ) var precipitation : Int? = null

)