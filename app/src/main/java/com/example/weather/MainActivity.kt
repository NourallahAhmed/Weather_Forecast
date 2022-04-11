package com.example.weather

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.weather.Home.HomeView.Home_View
import com.example.weather.Home.HomeViewModel.HomeViewModel
import com.example.weather.Home.HomeViewModel.HomeViewModelFactory

import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener

class MainActivity : AppCompatActivity() {
    var radioGroup :RadioGroup ?=null
    lateinit var gps_Button: RadioButton
    lateinit var map_Button: RadioButton
    lateinit var fusedLocationProviderClient : FusedLocationProviderClient
    lateinit var gotohomebtn : Button

    var Latitude :Double = 0.0
    var Longitude : Double = 0.0
    var PERMISSION_ID = 45

    lateinit var homeViewModel : HomeViewModel
    lateinit var myfactory: HomeViewModelFactory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initcomp()

        map_Button.setOnClickListener(View.OnClickListener {

        })

        gps_Button.setOnClickListener(View.OnClickListener {

            //step1 create location;

            //step1 create location;

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

           getUserLocation()

            println("from main activty $Latitude  - $Longitude")


        })

        gotohomebtn.setOnClickListener(View.OnClickListener {

            /*
            * set to main activity view model to set the data before go to home
            */


            //intent ( current , distinction)
            var homeintent  = Intent(this@MainActivity,Home_View::class.java)

            println("from main activty $Latitude  - $Longitude")

            homeintent.putExtra("lon",Longitude)
            homeintent.putExtra("lat",Latitude)

            startActivity(homeintent)

        })
    }

    private fun getUserLocation() {

        //step2 check the permissions ==> function
        if (CheckPermissions() == true) {

            //check if the location enabled

            isLocEnabled()

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            fusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(OnCompleteListener<Location?> { task ->
                    val location = task.result //lastlocation
                    if (location == null) // first time using this mobile
                    {
                        getNewLoctaion()
                    } else {
                        Latitude = location.latitude
                        Longitude= location.longitude

                        /*
                        *
                        * send to view model to get the data
                        * */
                    }
                })
        }
    }

    private val myLoctionCallBack: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val myLocation = locationResult.lastLocation
//            Latitude.setText(myLocation.latitude.toString() + " ")
//            Longitude.setText(myLocation.longitude.toString() + " ")
        }
    }

    private fun getNewLoctaion() {

        //initializing locationrequest

        //critria
        val mylocationRequest = LocationRequest.create()

        //GPS
        mylocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mylocationRequest.interval = 5
        mylocationRequest.fastestInterval = 0
        mylocationRequest.numUpdates = 1

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            mylocationRequest, myLoctionCallBack,
            Looper.myLooper()!!
        )
    }

    fun isLocEnabled() {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        val gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val internetEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (!gpsEnabled || !internetEnabled) {
            enableLocationSettings()
        }
    }

    //Enabled
    fun enableLocationSettings() {
//
        val settings = Intent(Settings.ACTION_LOCALE_SETTINGS)
        val settings2 = Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS)
        startActivity(settings)
        startActivity(settings2)
    }

    private fun CheckPermissions(): Boolean {
        //step2
        var Check = false

        //Activity Comapt ---> checkselfpermission
        //                --->requestPersmission

        // A) check permissions

        //Activity Comapt ---> checkselfpermission
        //                --->requestPersmission

        // A) check permissions
        Check = if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            //permissions available
            true
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), PERMISSION_ID
            )
            true
        }

        return Check

    }

    private fun initcomp() {
        radioGroup = findViewById(R.id.radioGroup)
        gps_Button = findViewById(R.id.GPS_ID)
        map_Button = findViewById(R.id.map_ID)
//        gotohomebtn=findViewById(R.id.to_home_btn)
    }


}