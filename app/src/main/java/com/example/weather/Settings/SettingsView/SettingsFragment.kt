package com.example.weather.Settings.SettingsView

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.weather.Home.HomeView.Home_View
import com.example.weather.MainActivity2
import com.example.weather.MapFragment
import com.example.weather.R
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import java.util.*

class SettingsFragment : Fragment() {

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

    lateinit var englishbtn: RadioButton
    lateinit var Arabicbtn: RadioButton
    lateinit var celsius: RadioButton
    lateinit var kelvin: RadioButton
    lateinit var fahrenheit: RadioButton
    lateinit var meterPerSec: RadioButton
    lateinit var milePerHour: RadioButton
    lateinit var GPS: RadioButton
    lateinit var Map: RadioButton

    //for shared preferences
    lateinit var settings : SharedPreferences
    lateinit var editor : SharedPreferences.Editor
    var langPref  : Boolean = false
    var tempunit :String ?=null
    var WindSpeed :Boolean = true // true -> Meter / sec


    //for gps

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var Latitude: Double =0.0
    var Longitude: Double =0.0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.settingpage, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var context = view.context
        // if not exist will create one
        settings = requireActivity().getSharedPreferences(Home_View.PREFS_NAME,0)
        editor = settings.edit()

        initComp(view)

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

        //_______Unit Config_______

        //Celsius and wind speed in meter/sec, use units=metric
        celsius.setOnClickListener {
            editor.putString("tempunit","metric")
            meterPerSec.isChecked=true
            println("done")
        }
        //Fahrenheit and wind speed in miles/hour, use units=imperial
        fahrenheit.setOnClickListener {
            editor.putString("tempunit","imperial")
            milePerHour.isChecked=true
            println("done")

        }

        //Kelvin and wind speed in meter/sec is used by default,
        kelvin.setOnClickListener {
            editor.putString("tempunit","standard")
            meterPerSec.isChecked=true
            println("done")

        }

        //________WindSpeed handling____________

        meterPerSec.setOnClickListener {
            editor.putString("tempunit","standard")
            kelvin.isChecked=true
        }

        milePerHour.setOnClickListener {
            editor.putString("tempunit","imperial")
            fahrenheit.isChecked=true
        }


        //_______________MAP OR GPS_____________
        GPS.setOnClickListener {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
            getUserLocation()
        }
        Map.setOnClickListener {
            if(checkForInternet(requireContext())){
                val mFragment = MapFragment()
                getFragmentManager()?.beginTransaction()?.
                replace(MainActivity2.x , mFragment)
                    ?.commit();}
            else{
                Toast.makeText(requireContext(),R.string.checkinternet , Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun initComp(view: View) {
        englishbtn=view.findViewById(R.id.eng_Id)
        Arabicbtn=view.findViewById(R.id.Arab_ID)
        celsius=view.findViewById(R.id.calsius_Id)
        kelvin=view.findViewById(R.id.kelvin_Id)
        fahrenheit= view.findViewById(R.id.fahrenheit_Id)
        meterPerSec=view.findViewById(R.id.meterPerSec_Id)
        milePerHour=view.findViewById(R.id.MilePreHour_Id)
        GPS=view.findViewById(R.id.gpsSet_ID)
        Map=view.findViewById(R.id.mapSet_ID)
    }

    private fun getUserLocation() {
        //step2 check the permissions ==> function
        if (CheckPermissions() == true) {
            //check if the location enabled
            isLocEnabled()
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
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
                        Longitude = location.longitude
                        //send to home
                        /*
                        * map_lat the same key from the map -- will not differ in implementation
                        * */
                        editor.putString("map_lat",Latitude.toString())
                        println("LAT= ${Latitude}")
                        println("LONG= ${Longitude}")
                        editor.putString("map_long",Longitude.toString())
                    }
                })
        }
    }
    //mobile permission
    private fun CheckPermissions(): Boolean {
        //step2
        var Check = false
        //Activity Comapt ---> checkselfpermission
        //                --->requestPersmission
        // A) check permissions

        Check = if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            //permissions available
            true
        } else {
            val PERMISSION_ID = 45
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ), PERMISSION_ID
            )
            true
        }

        return Check

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

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
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

    private fun isLocEnabled() {
        val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val internetEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (!gpsEnabled || !internetEnabled) {
            enableLocationSettings()
        }
    }
    //Enabled
    private fun enableLocationSettings() {
        val settings = Intent(Settings.ACTION_LOCALE_SETTINGS)
        val settings2 = Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS)
        startActivity(settings)
        startActivity(settings2)
    }

    private val myLoctionCallBack: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val myLocation = locationResult.lastLocation
            editor.putString("map_long",Longitude!!.toString())
            editor.putString("map_lat",Latitude!!.toString())
        }
    }


    private fun checkForInternet(context: Context): Boolean {

        // register activity with the connectivity manager service
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // if the android version is equal to M
        // or greater we need to use the
        // NetworkCapabilities to check what type of
        // network has the internet connection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Returns a Network object corresponding to
            // the currently active default data network.
            val network = connectivityManager.activeNetwork ?: return false

            // Representation of the capabilities of an active network.
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                // Indicates this network uses a Wi-Fi transport,
                // or WiFi has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                // Indicates this network uses a Cellular transport. or
                // Cellular has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                // else return false
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }
    override fun onStop() {
        super.onStop()
        editor.commit()

    }
}