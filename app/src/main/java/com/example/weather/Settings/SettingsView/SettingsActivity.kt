package com.example.weather.Settings.SettingsView

import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.example.weather.Home.HomeView.Home_View
import com.example.weather.R
import java.util.*

class SettingsActivity : AppCompatActivity() {

/*
*
* Temperature is available in Fahrenheit, Celsius and Kelvin units.
* Wind speed is available in miles/hour and meter/sec.

* For temperature in Fahrenheit and wind speed in miles/hour, use units=imperial
* For temperature in Celsius and wind speed in meter/sec, use units=metric
* Temperature in Kelvin and wind speed in meter/sec is used by default,
* so there is no need to use the units parameter in the API call if you want this
*
* */


    lateinit var englishbtn:RadioButton
    lateinit var Arabicbtn:RadioButton
    lateinit var celsius:RadioButton
    lateinit var kelvin:RadioButton
    lateinit var fahrenheit:RadioButton
    lateinit var meterPerSec:RadioButton
    lateinit var milePerHour:RadioButton

    lateinit var GPS:RadioButton
    lateinit var Map:RadioButton

    //for shared preferences
    lateinit var settings : SharedPreferences
    lateinit var editor : SharedPreferences.Editor
    var langPref  : Boolean = false
    var tempunit :String ?=null
    var WindSpeed :Boolean = true // true -> Meter / sec


    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.settingpage)
            // if not exist will create one
            settings = getSharedPreferences(Home_View.PREFS_NAME,0)
            editor = settings.edit()

            initComp()

            //_______Language Config_______

            englishbtn.setOnClickListener {
            editor.putString("lang","en")
                var locale = Locale("en")
                val res = resources
                val dm = res.displayMetrics
                val conf = res.configuration
                conf.locale = locale
                res.updateConfiguration(conf, dm)
                println("done")
        }
            Arabicbtn.setOnClickListener {
                editor.putString("lang","ar")
                var locale = Locale("ar")
                val res = resources
                val dm = res.displayMetrics
                val conf = res.configuration
                conf.locale = locale
                res.updateConfiguration(conf, dm)
                println("done")

            }

            //______Unit Config_______

            celsius.setOnClickListener {
                editor.putString("tempunit","metric")
                println("done")

            }
            fahrenheit.setOnClickListener {
                editor.putString("tempunit","imperial")
                println("done")

            }
            kelvin.setOnClickListener {
               editor.putString("tempunit","standard")
                println("done")

            }
            //________Map Config
    }

    private fun initComp() {
        englishbtn=findViewById(R.id.eng_Id)
        Arabicbtn=findViewById(R.id.Arab_ID)
        celsius=findViewById(R.id.calsius_Id)
        kelvin=findViewById(R.id.kelvin_Id)
        fahrenheit= findViewById(R.id.fahrenheit_Id)
        meterPerSec=findViewById(R.id.meterPerSec_Id)
        milePerHour=findViewById(R.id.MilePreHour_Id)
        GPS=findViewById(R.id.gpsSet_ID)
        Map=findViewById(R.id.mapSet_ID)
    }

    override fun onStop() {
        super.onStop()
        editor.commit()
    }


}