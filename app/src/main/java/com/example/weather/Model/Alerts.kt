package  com.example.weatherapp.models.responseDataModel

import com.google.gson.annotations.SerializedName


data class Alerts (

  @SerializedName("sender_name" )
  var senderName  : String?           = null,
  @SerializedName("event"       )
  var event       : String?           = null,
  @SerializedName("start"       )
  var start       : Int?              = null,
  @SerializedName("end"         )
  var end         : Int?              = null,
  @SerializedName("description" )
  var description : String?           = null,
  @SerializedName("tags"        )
  var tags        : ArrayList<String> = arrayListOf()

)