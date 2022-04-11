package com.example.weather.Alert

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.example.weather.DataBase.ConcreteLocalSource
import com.example.weather.Home.HomeViewModel.HomeViewModel
import com.example.weather.Home.HomeViewModel.HomeViewModelFactory
import com.example.weather.Model.Alerts
import com.example.weather.Model.WeatherModel
import com.example.weather.Network.Weather_Client
import com.example.weather.R
import com.example.weather.Repo.Repo
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit

class NotificationAlert(appcontext:Context , params:WorkerParameters) : CoroutineWorker(appcontext , params) {
    /*
    *
    *  Work is defined in WorkManager via a WorkRequest.
    *  In order to schedule any work with WorkManager
    *  you must first create a WorkRequest object and then enqueue it.
    *
    * */


    /*
    * OneTimeWorkRequest is useful for scheduling non-repeating work
    *
    * PeriodicWorkRequest is more appropriate for scheduling work that repeats
    * on some interval
    *
    * */

    lateinit var myfactory: HomeViewModelFactory
    lateinit var homeViewModel :HomeViewModel

    lateinit var settings : SharedPreferences
    lateinit var editor : SharedPreferences.Editor
    val PREFS_NAME= "mysharedfile"

    lateinit var model :WeatherModel

    val context= appcontext
    val NOTIFICATION_ID=7000
    val CHANEL_ID="channel_ID"
    val CHANNEL_NAME="Alerts"

    companion object{
        //network connection
        //to show updates
        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).setRequiresBatteryNotLow(true)  //When set to true, your work will not run if the device is in low battery mode.
            .build()

        //periodically
        // (1440 , TimeUnit.MINUTES) --> 24 hours
        val periodWorkRequest = PeriodicWorkRequestBuilder<NotificationAlert>(1,TimeUnit.MINUTES)
        .setConstraints(constraints)


        //on time request
        val oneTimeWorkRequest = OneTimeWorkRequestBuilder<NotificationAlert>()
            .setConstraints(constraints)
            .build()
    }
    override suspend fun doWork(): Result {
        //simulate heavy work
        try{
//            println("from dowork notif ${alert.description}")

            val data = inputData
//            val day = inputData.getString("day")
            val hour = inputData.getString("hour")

            println("from dowork notif  and $hour")

            //get the Lat and Lon to send request from shared pref

            settings = context.getSharedPreferences(PREFS_NAME,0)
            var unit = settings.getString("tempunit","standard")
            var lang = settings.getString("lang","en")
            //from map only (( now ))
            var Latitude = settings.getString("map_lat", "0.0")?.toDouble() ?: 0.0
            var Longitude = settings.getString("map_long","0.0")?.toDouble() ?: 0.0


            //send the request to get the model with recent data

            var repo = Repo.getInstance( context,
                ConcreteLocalSource(context),
                Weather_Client.getinstance()!!)


            runBlocking {
                model = repo.getNetworkData(lon=Longitude,lat = Latitude,unit = unit!! , lang = lang!!)
            }



            println("the model : $model")
            var alert = model.alerts?.get(0)

            println("the alert : ${alert?.description}")


            //create required steps for notification
            createChannel(CHANEL_ID,CHANNEL_NAME, context)

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            var alertmsg :String = "No Alerts"

            if(alert!=null){
                alertmsg= "${alert?.description}"
            }

            notificationManager.sendMessage(alertmsg,context)
        }catch (e :Exception){
            e.printStackTrace()
            Result.failure()
        }
        return Result.success()
    }


    fun NotificationManager.sendMessage(messageBody:String , appcontext: Context){
        val builder = NotificationCompat.Builder(appcontext,CHANNEL_NAME)
        println("from the notification manager : $messageBody")
        builder.apply {
            setSmallIcon(R.drawable.cloud)
            setContentTitle(appcontext.getString(R.string.notification_titte))
            setContentText(messageBody)
            setStyle(NotificationCompat.BigTextStyle().bigText(messageBody))
            setChannelId(CHANEL_ID)
        }
        notify(NOTIFICATION_ID,builder.build())
    }
    private fun createChannel(chanelId: String, channelName: String, context: Context) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(chanelId,channelName
             , NotificationManager.IMPORTANCE_HIGH)
             notificationChannel.apply {
                 name="Alert Notification"
                 enableLights(true)
                 enableVibration(true)
                 description="description "
             }

            val notificationManager: NotificationManager=
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }


}