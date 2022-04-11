package com.example.weather.Home.HomeView

//import com.example.weather.Model.Pojo

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.weather.DataBase.ConcreteLocalSource
import com.example.weather.Home.HomeViewModel.HomeViewModel
import com.example.weather.Home.HomeViewModel.HomeViewModelFactory
import com.example.weather.Model.Hourly
import com.example.weather.Model.WeatherModel
import com.example.weather.Network.Weather_Client
import com.example.weather.R
import com.example.weather.Repo.Repo
import com.example.weather.Settings.SettingsView.SettingsActivity
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationView
import java.text.SimpleDateFormat
import java.util.*


class Home_View : AppCompatActivity()
{

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
//    var weekAdapter = WeekRecyclerAdapter(this, unit)
    var hourAdapter = HourRecyclerAdapter(this)
    lateinit var navDrawer: NavigationView

    //______________________________________
    lateinit var layoutManager: LinearLayoutManager
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var Latitude: Double = 0.0
    var Longitude: Double = 0.0

    //-------------------
    lateinit var homeViewModel: HomeViewModel
    lateinit var myfactory: HomeViewModelFactory


    //for language settings
    lateinit var locale: Locale
    var lang = true //true -> English
    var check=true //true -> first time
    var check2=false //false -> dont  refrech
    var context = this

    //for shared preferences
    lateinit var settings : SharedPreferences
    var langPref  : String?=null
    var tempunit :String ?=null
    var WindSpeed :Boolean = true // true -> Meter / sec


    //for navigation
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var toggle: ActionBarDrawerToggle

    //for google map
    companion object {
        private const val REQUEST_PERMISSIONS_REQUEST_CODE = 1
         val PREFS_NAME= "mysharedfile"
        var LangToAdapter ="en"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        myfactory = HomeViewModelFactory(Repo.getInstance( this, ConcreteLocalSource(this), Weather_Client.getinstance()!!))
        homeViewModel = ViewModelProvider(this, myfactory).get(HomeViewModel::class.java)

        initComp()
        //_____ Navigation Drawer_____

        toggle= ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navigationView=findViewById(R.id.navView)
        navigationView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home_ID2 -> {startActivity(Intent(this, Home_View::class.java))}
                R.id.setting2 -> {startActivity(Intent(this, SettingsActivity::class.java))}
            }
            true
        }


        //getting shared pref

        /*
        * use shared pref to get the settings -> lang , units
        *
        * update the shared pref from settings page and receive it here
        *
        * */

        // if not exist will create one
        settings = getSharedPreferences(PREFS_NAME,0)

        langPref=settings.getString("lang","en") // default english
        Home_View.LangToAdapter= langPref!!

        tempunit=settings.getString("tempunit","standard")

        //WindSpeed= settings.getBoolean("windspeed",true) // true -> Meter/sec

        //check the pref
        println("fromsharedpref lang = $lang")
        println("fromsharedpref temp = $tempunit")

        // ____________________________________________

        /*
        *
        *   check if it the first time show the dialoge if not don`t show it
        *   using intent (MO2Qatan)
        *   send intent by check from the settings
        *   and fav
        *   and alert
        *
        * */

        check=intent.getBooleanExtra("check",true)

        check2=intent.getBooleanExtra("refresh",false)

        /*
         *
         * Means that its not the first time the user enter
         *
         * check the dialog and the refresh of the Activity
         *
         * */
        if(check ==true){
            showSettingDialog()

        }

        if(check2==true){
            languageSettings()
        }

//
//        if(langPref.equals("ar")){
//            /*
//            * to change some words
//            *
//            * if it english i do
//            * */
//            languageSettings()
//        }


        //________________________________________________
        //when refresh the app
        //check the language
        //lang= intent.getBooleanExtra("lang",true)

        //_______________________________________________



       if(checkForInternet(this)) {
           SendRequest(lang = langPref!!, unit = tempunit!!)
       }
        else{
            SendLocal()

            Toast.makeText(this,"No internet",Toast.LENGTH_LONG).show()
           println("no Internet")
       }
        //_________________________________________________
    }

    private fun SendLocal() {
        homeViewModel.getLocal(Longitude!!, Latitude!!)

        homeViewModel.dataimmutable.observe(this) { data ->
            if (data != null) {
                setDataOnHomeScreen(data)
                println("__________________________get the data______________")
                //update the last data into the DB
                homeViewModel.inserttoDB(data)
            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    //send to view model
    @SuppressLint("SetTextI18n")

    private fun SendRequest(lang :String , unit:String) {
//        homeViewModel.setLocation(
//            long = Longitude,
//            lat = Latitude )

        // send the network request
        //manage the language coming from the Api


        homeViewModel.getrequest(lang=lang, unit=unit, long = Longitude, lat = Latitude)


        /*
 * UI not responding
 *
 * */
        // receiving the data
        homeViewModel.dataimmutable.observe(this) { data ->
            if (data != null) {
                setDataOnHomeScreen(data)
                println("__________________________get the data______________")
                //update the last data into the DB
//                homeViewModel.inserttoDB(data)
            }

        }
    }

    private fun setDataOnHomeScreen(data: WeatherModel) {

        // set up the data in home screen
        location?.text = data.timezone

        var weather = data.current?.weather?.get(0)
        description?.text = weather?.description

        var iconurl = "http://openweathermap.org/img/wn/${weather?.icon}@2x.png";

        Glide.with(this.applicationContext).load(iconurl).apply(
            RequestOptions().override(500, 500).placeholder(R.drawable.ic_launcher_background))
            .dontAnimate().into(currentImage!!)

        tempreture?.text = data.current?.temp.toString() // + "℃"

        humidity?.text = data.current?.humidity.toString() + "%"

        pressure?.text = data.current?.pressure.toString() + "hpa"

        wind?.text = data.current?.windSpeed.toString() + "m/s"

        ultraviolet?.text = data.current?.uvi.toString() + "m"

        cloud?.text = data.current?.clouds.toString() + "%"

        visiblity?.text = data.current?.visibility.toString()


        sethourRecycler(data.hourly)
//        setWeekRecycler(data.daily)

    }

    private fun showSettingDialog() {
        val dialogBuilder = AlertDialog.Builder(this)

        var dialogView = LayoutInflater.from(this).inflate(R.layout.activity_main, null)
        var gps = dialogView.findViewById<RadioButton>(R.id.GPS_ID)
        var map = dialogView.findViewById<RadioButton>(R.id.map_ID)
        dialogBuilder.setPositiveButton("Done") { dialog, which ->
            //if lan and lat == null
        }

        gps.setOnClickListener {

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

            getUserLocation()
        }
        map.setOnClickListener {

//            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
//
//            val permissionState = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
//            if (permissionState != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_PERMISSIONS_REQUEST_CODE)
//            }

        }
        dialogBuilder.setView(dialogView)
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    //for google map
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_PERMISSIONS_REQUEST_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    getLocationByMap()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocationByMap() {


        val gmmIntentUri = Uri.parse("google.streetview:cbll=$Latitude,$Longitude")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)

        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent)


        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                Longitude = it.longitude
                Latitude = it.latitude


                println(" from map $Longitude + $Latitude")
            }
        }
    }

    private fun getUserLocation() {


        println("getUserLocation")
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
                        Longitude = location.longitude

                        /*
                        *
                        * send to view model to get the data
                        * */
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

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
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
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        val gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val internetEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (!gpsEnabled || !internetEnabled) {
            enableLocationSettings()
        }
    }

    //Enabled
    private fun enableLocationSettings() {
//
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

//    private fun setWeekRecycler(daily: List<Daily>) {
//        layoutManager = LinearLayoutManager(this)
//
//        // call the method addItemDecoration with the
//        // recyclerView instance and add default Item Divider
//
//        layoutManager.orientation = RecyclerView.VERTICAL
//        weekRecycler?.addItemDecoration(
//            DividerItemDecoration(
//                baseContext,
//                layoutManager.orientation
//            )
//        )
//        weekAdapter.week = daily
//        weekRecycler!!.adapter = weekAdapter
//        weekRecycler!!.layoutManager = layoutManager
//    }

    private fun sethourRecycler(hourly: List<Hourly>) {
        layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = RecyclerView.HORIZONTAL
        hourRecycler?.addItemDecoration(
            DividerItemDecoration(
                baseContext,
                layoutManager.orientation
            )
        )
        hourAdapter.hours = hourly
        hourRecycler!!.adapter = hourAdapter
        hourRecycler!!.layoutManager = layoutManager
    }

    //find by id
    private fun initComp() {

        drawerLayout=findViewById(R.id.drawerLayout)
        navigationView=findViewById(R.id.navView)
        location = findViewById(R.id.Location_ID)
        description = findViewById(R.id.Desc_ID)
        tempreture = findViewById(R.id.Temp_ID)
        humidity = findViewById(R.id.Humdidity_Id)
        pressure = findViewById(R.id.Pressure_Id)
        wind = findViewById(R.id.Wind_Id)
        visiblity = findViewById(R.id.Visibility_Id)
        cloud = findViewById(R.id.Cloud_Id)
        ultraviolet = findViewById(R.id.UltraViolet_Id)
        weekRecycler = findViewById(R.id.Week_Recycler)
        hourRecycler = findViewById(R.id.Hour_Recycler)
        currentImage = findViewById(R.id.currentImageId)
        currentData=findViewById(R.id.Day_Id)

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
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            //permissions available
            true
        } else {
            val PERMISSION_ID = 45
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ), PERMISSION_ID
            )
            true
        }

        return Check

    }

    //deleted
    fun toSetting(view: View){

        startActivity(Intent(this, SettingsActivity::class.java))
    }

    fun languageSettings(){

        println("language flage -> $lang")

        if (langPref!!.equals("ar")) { //switch to arabic

            Toast.makeText(this@Home_View, "Arabic!", Toast.LENGTH_SHORT).show();
            var locale = Locale("ar")
            val res = resources
            val dm = res.displayMetrics
            val conf = res.configuration
            conf.locale = locale
            res.updateConfiguration(conf, dm)
            val refresh = Intent(this, Home_View::class.java)
            refresh.putExtra("refresh",false)
            println("language flage -> $lang")
            startActivity(refresh)
        } else {

            Toast.makeText(this@Home_View, "English!", Toast.LENGTH_SHORT).show();
            var locale = Locale("en")
            val res = resources
            val dm = res.displayMetrics
            val conf = res.configuration
            conf.locale = locale
            res.updateConfiguration(conf, dm)
            val refresh = Intent(this, Home_View::class.java)
            //using put extra bec of the refresh
            refresh.putExtra("refresh",false)
            startActivity(refresh)
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
}

//---------------> Button Bar <----------------
//        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigatin_view)
//        //val navController = findNavController(R.id.nav_fragment)
//        //BottomNavigationView.OnNavigationItemSelectedListener { item ->
////            when(item.itemId) {
////                R.id.home_ID2 -> {
////                    // Respond to navigation item 1 click
////                    startActivity(Intent(this, Home_View::class.java))
////                    true
////                }
////                R.id.setting2 -> {
////                    // Respond to navigation item 2 click
////                    startActivity(Intent(this, SettingsActivity::class.java))
////
////                    true
////                }
////                else -> false
////            }
////        }
//bottomNavigationView.setupWithNavController(navController)