package com.example.weather.Alert.AlertView

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.work.*
import com.example.weather.Alert.NotificationAlert
import com.example.weather.R
import java.util.*
import java.util.concurrent.TimeUnit


class Alert_View : Fragment() {

    lateinit var timePicker: TimePicker
    lateinit var datePicker: DatePicker
    lateinit var save:Button
    lateinit var settings : SharedPreferences
    lateinit var editor : SharedPreferences.Editor

    var requests = mutableListOf<OneTimeWorkRequest>()
    val PREFS_NAME= "mysharedfile"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alert__view, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settings = requireActivity().getSharedPreferences(PREFS_NAME,0)
        editor = settings.edit()

        timePicker=view.findViewById(R.id.timePicker)
        datePicker=view.findViewById(R.id.calendarView)
        view.findViewById<Button?>(R.id.savebtn).setOnClickListener {
            //hour
            val daytime = "${timePicker.hour}:${timePicker.minute}"


            //_________________________________________________________________________
            //date
            val day = datePicker.dayOfMonth
            val month = datePicker.month + 1
            val year = datePicker.year
            val date: String

            date = if (month <= 9) "$day/0$month/$year" else {
                "$day/$month/$year" }


            val calendar = Calendar.getInstance()
            calendar[0, 0, 0, timePicker.hour] = timePicker.minute


            val pickeddate = date.split("/".toRegex()).toTypedArray()

            val enddate = Calendar.getInstance()
            enddate[pickeddate[2].toInt(), pickeddate[1].toInt(), pickeddate[0].toInt(), timePicker.hour] = timePicker.minute

            //get today`s date
            val today = Calendar.getInstance()

            println("data ${today.get(Calendar.HOUR)} ,${today.get(Calendar.MINUTE)}")

            // the difference
            var diffInMinutes = (enddate.timeInMillis - today.timeInMillis) / 60000
            diffInMinutes -= 44640

            println("diff in time $diffInMinutes")


            //passing data to work manager
            // passing the end data and hour
            var data = Data.Builder()
                .putString("hour", daytime)
                .build()


            // for today
            var workRequest = OneTimeWorkRequestBuilder<NotificationAlert>()
                .setConstraints(NotificationAlert.constraints)
                .setInitialDelay(diffInMinutes, TimeUnit.MINUTES)
                .setInputData(data).build()


            if (diffInMinutes > 0) {
                requests.add(workRequest)
            }


            var  numofdays =  enddate.get(Calendar.DAY_OF_MONTH) - today.get(Calendar.DAY_OF_MONTH)
            println(numofdays)

            //depends on num of days
            for (i in 1..(numofdays+1)) {
                //increasing by the days
                val duration = Math.abs(diffInMinutes + 1440 * i)
                var workRequest = OneTimeWorkRequest.Builder(NotificationAlert::class.java)
                    .setInitialDelay(duration, TimeUnit.MINUTES)
                    .setInputData(data)
                    .build()
                requests.add(workRequest)
            }

            //save it in shared pref if needed

            editor.putString("ALertTime", daytime)
            editor.putBoolean("Alert", true)


//            work manager instance
            WorkManager.getInstance(requireContext()).enqueue(requests)

        }
    }


    private fun calculateFlex(hourOfTheDay: Int, periodInDays: Int): Long {

        // Initialize the calendar with today and the preferred time to run the job.
        val cal1 = Calendar.getInstance()
        cal1[Calendar.HOUR_OF_DAY] = hourOfTheDay
        cal1[Calendar.MINUTE] = 0
        cal1[Calendar.SECOND] = 0

        // Initialize a calendar with now.
        val cal2 = Calendar.getInstance()
        if (cal2.timeInMillis < cal1.timeInMillis) {
            // Add the worker periodicity.
            cal2.timeInMillis = cal2.timeInMillis + TimeUnit.DAYS.toMillis(periodInDays.toLong())
        }
        val delta = cal2.timeInMillis - cal1.timeInMillis
        return if (delta > PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS) delta else PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS
    }

    override fun onStop() {
        super.onStop()
        editor.commit()
    }
}

