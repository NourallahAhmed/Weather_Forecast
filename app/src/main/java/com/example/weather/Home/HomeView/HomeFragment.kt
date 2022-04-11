package com.example.weather.Home.HomeView

//import com.example.weather.Model.Pojo

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Context.LOCATION_SERVICE
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
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.weather.Alert.NotificationAlert
import com.example.weather.DataBase.ConcreteLocalSource
import com.example.weather.Home.HomeViewModel.HomeViewModel
import com.example.weather.Home.HomeViewModel.HomeViewModelFactory
import com.example.weather.MainActivity2
import com.example.weather.MapFragment
import com.example.weather.Model.Daily
import com.example.weather.Model.Hourly
import com.example.weather.Model.WeatherModel
import com.example.weather.Network.Weather_Client
import com.example.weather.R
import com.example.weather.Repo.Repo
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {

    // ______________ xml Component ______________
    var location: TextView? = null
    var currentData: TextView? = null
    var description: TextView? = null
    var tempreture: TextView? = null
    var humidity: TextView? = null
    var pressure: TextView? = null
    var wind: TextView? = null
    var visiblity: TextView? = null
    var cloud: TextView? = null
    var ultraviolet: TextView? = null
    var currentImage: ImageView? = null
    var languagebtn: Button? = null
    var weekRecycler: RecyclerView? = null
    var hourRecycler: RecyclerView? = null
    var unit:String ?=null
    var unitWind:String ="m/s"

    lateinit var weekAdapter :WeekRecyclerAdapter
    lateinit var hourAdapter : HourRecyclerAdapter

    //______________________________________

    lateinit var layoutManager: LinearLayoutManager
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var Latitude: Double =0.0
    var Longitude: Double =0.0
    lateinit var homeViewModel: HomeViewModel
    lateinit var myfactory: HomeViewModelFactory

    //for language settings
    lateinit var locale: Locale
    var lang = true //true -> English
    var checkDialog=true //true -> first time


    //for shared preferences
    lateinit var settings : SharedPreferences
    lateinit var editer :SharedPreferences.Editor

    var langPref  : String?=null
    var tempunit :String ?=null
    var WindSpeed :String ?=null

    companion object {
        val PREFS_NAME= "mysharedfile"
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home,container,false)
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var context = view.context
        myfactory = HomeViewModelFactory(
            Repo.getInstance( requireContext(),
                ConcreteLocalSource(requireContext()),
                Weather_Client.getinstance()!!))
        homeViewModel = ViewModelProvider(this, myfactory).get(HomeViewModel::class.java)

        initComp(view)

        //__________________________getting shared pref__________________________________

        /*
        * use shared pref to get the settings -> lang , units
        *
        * update the shared pref from settings page and receive it here
        *
        * */

        // if not exist will create one
        settings = requireContext().getSharedPreferences(PREFS_NAME,0)
        editer = settings.edit()
        Latitude = settings.getString("map_lat", "0.0")?.toDouble() ?: 0.0
        Longitude = settings.getString("map_long","0.0")?.toDouble() ?: 0.0
        checkDialog=settings.getBoolean("checkDialog",true)
        langPref=settings.getString("lang","en") // default english
        tempunit=settings.getString("tempunit","standard")


        //update the string of Unit according to the unit received
        if(tempunit.equals("imperial")) {
            unit="F"
            unitWind="m/h"
        }
        if(tempunit.equals("metric"))  unit="℃"
        if(tempunit.equals("standard"))  unit="K"


        //send the unit to the weekadapter to update the unit
        weekAdapter = WeekRecyclerAdapter(context , unit)
        hourAdapter = HourRecyclerAdapter(context)

        //check the pref
        println("from home Lat: ${Latitude}")
        println("from home Long: ${Longitude}")

        println("fromsharedpref dialog = ${checkDialog}")
        println("fromsharedpref long = $Longitude")
        println("fromsharedpref lat = $Latitude")
        println("fromsharedpref lang = $lang")
        println("fromsharedpref temp = $tempunit")



        //__________________________________HOMESETTINGS______________________________________

//
//        if(langPref.equals("ar")){
//            /*
//            * to change some words
//            *
//            * if it english i do
//            * */
//            languageSettings()
//        }
// ________________________________________________

        //dialog
        if(checkDialog==true){
            showSettingDialog()
            editer.putBoolean("checkDialog",false)
        }

        //__________________________________Internet Connection______________________
        if(checkForInternet(requireContext())) {
            SendRequest(lang = langPref!!, unit = tempunit!!)
        }
        else{
            SendLocal()
            Toast.makeText(requireContext(),"No internet",Toast.LENGTH_LONG).show()
        }
    }

    private fun SendLocal() {
        println("sendlocal : long = $Longitude and Lat:$Latitude")
        homeViewModel.getLocal(Longitude!!,Latitude!!)

        homeViewModel.dataimmutable.observe(viewLifecycleOwner){
            setDataOnHomeScreen(it)
        }
    }

    //send to view model
    @SuppressLint("SetTextI18n")
    private fun SendRequest(lang :String , unit:String) {

        // send the network request
        //manage the language coming frosm the Api
        homeViewModel.getrequest(lang=lang , unit=unit ,long = Longitude!!,
            lat = Latitude!!)
        /*
        * UI not responding
        */
        // receiving the data
        homeViewModel.dataimmutable.observe(requireActivity()) { data ->
            if (data != null) {
                setDataOnHomeScreen(data)
                //update the last data into the DB
                homeViewModel.inserttoDB(data)
//                WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
//                    "try",ExistingPeriodicWorkPolicy.KEEP)
            }

        }
    }

    private fun setDataOnHomeScreen(data: WeatherModel) {

        // set up the data in home screen
        location?.text = data.timezone
        println("timezone ${data.timezone}")

        var weather = data.current?.weather?.get(0)
        description?.text = weather?.description

        var iconurl = "http://openweathermap.org/img/wn/${weather?.icon}@2x.png";

        Glide.with(requireContext()).load(iconurl).apply(
            RequestOptions().override(500, 500).placeholder(R.drawable.ic_launcher_background))
            .dontAnimate().into(currentImage!!)

        tempreture?.text = data.current?.temp.toString()  + unit

        humidity?.text = data.current?.humidity.toString() + "%"

        pressure?.text = data.current?.pressure.toString() + "hpa"

        wind?.text = data.current?.windSpeed.toString() + unitWind

        ultraviolet?.text = data.current?.uvi.toString() + "m"

        cloud?.text = data.current?.clouds.toString() + "%"

        visiblity?.text = data.current?.visibility.toString()


        sethourRecycler(data.hourly)
        setWeekRecycler(data.daily)

    }

    private fun showSettingDialog() {
        val dialogBuilder = AlertDialog.Builder(requireContext())

        var dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.activity_main, null)
        var gps = dialogView.findViewById<RadioButton>(R.id.GPS_ID)
        var map = dialogView.findViewById<RadioButton>(R.id.map_ID)

        gps.setOnClickListener {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
            getUserLocation()
        }

        map.setOnClickListener {
            println("MAP")
            if(checkForInternet(requireContext())){
                val mFragment = MapFragment()
                getFragmentManager()?.beginTransaction()?.
                replace(MainActivity2.x , mFragment)
                    ?.commit();}
            else{
                Toast.makeText(requireContext(),R.string.checkinternet , Toast.LENGTH_LONG).show()
            }

        }
        dialogBuilder.setView(dialogView)
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    private fun getUserLocation() {


        println("getUserLocation")
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

                        /*
                        *
                        * send to view model to get the data
                        * */

                        editer.putString("map_long",Longitude!!.toString())
                        editer.putString("map_lat",Latitude!!.toString())
                        SendRequest(lang=langPref!!,unit=tempunit!!)
                        println(" inside :  $Latitude , $Longitude")


                    }

                })
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
        val locationManager = requireActivity().getSystemService(LOCATION_SERVICE) as LocationManager
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
//            Latitude.setText(myLocation.latitude.toString() + " ")
//            Longitude.setText(myLocation.longitude.toString() + " ")
        }
    }

    private fun setWeekRecycler(daily: List<Daily>) {
        layoutManager = LinearLayoutManager(requireContext())

        // call the method addItemDecoration with the
        // recyclerView instance and add default Item Divider

        layoutManager.orientation = RecyclerView.VERTICAL
        weekRecycler?.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                layoutManager.orientation
            )
        )
        weekAdapter.week = daily
        weekRecycler!!.adapter = weekAdapter
        weekRecycler!!.layoutManager = layoutManager
    }

    private fun sethourRecycler(hourly: List<Hourly>) {
        layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = RecyclerView.HORIZONTAL
        hourRecycler?.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                layoutManager.orientation
            )
        )
        hourAdapter.hours = hourly
        hourRecycler!!.adapter = hourAdapter
        hourRecycler!!.layoutManager = layoutManager
    }

    //find by id
    private fun initComp(view:View) {
        location = view.findViewById(R.id.Location_ID)
        description = view.findViewById(R.id.Desc_ID)
        tempreture = view.findViewById(R.id.Temp_ID)
        humidity = view.findViewById(R.id.Humdidity_Id)
        pressure = view.findViewById(R.id.Pressure_Id)
        wind = view.findViewById(R.id.Wind_Id)
        visiblity = view.findViewById(R.id.Visibility_Id)
        cloud = view.findViewById(R.id.Cloud_Id)
        ultraviolet = view.findViewById(R.id.UltraViolet_Id)
        weekRecycler = view.findViewById(R.id.Week_Recycler)
        hourRecycler = view.findViewById(R.id.Hour_Recycler)
        currentImage = view.findViewById(R.id.currentImageId)
        currentData=view.findViewById(R.id.Day_Id)

        setDate()
    }

    @SuppressLint("SimpleDateFormat")
    private fun setDate() {
        var formatDate = SimpleDateFormat("dd/MM/yyyy ")
        var date =  Date()
        formatDate.setTimeZone(TimeZone.getTimeZone("IST"));
        // converting to IST or format the Date as IST
        currentData?.text=formatDate.format(date)
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
    fun languageSettings(){

        println("language flage -> $lang")

        if (langPref!!.equals("ar")) { //switch to arabic

            Toast.makeText(requireContext(), "Arabic!", Toast.LENGTH_SHORT).show();
            var locale = Locale("ar")
            val res = resources
            val dm = res.displayMetrics
            val conf = res.configuration
            conf.locale = locale
            res.updateConfiguration(conf, dm)


            // Reload current fragment
            val ft: FragmentTransaction = requireFragmentManager().beginTransaction()
            if (Build.VERSION.SDK_INT >= 26) {
                ft.setReorderingAllowed(false)
            }
            ft.detach(this).attach(this).commit()
        } else {

            Toast.makeText(requireContext(), "English!", Toast.LENGTH_SHORT).show();
            var locale = Locale("en")
            val res = resources
            val dm = res.displayMetrics
            val conf = res.configuration
            conf.locale = locale
            res.updateConfiguration(conf, dm)

            // Reload current fragment
            val ft: FragmentTransaction = requireFragmentManager().beginTransaction()
            if (Build.VERSION.SDK_INT >= 26) {
                ft.setReorderingAllowed(false)
            }
            ft.detach(this).attach(this).commit()
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
        editer.commit()
    }
}
