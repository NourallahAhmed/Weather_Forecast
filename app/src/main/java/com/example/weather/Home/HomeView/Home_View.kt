package com.example.weather.Home.HomeView

//import com.example.weather.Model.Pojo
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.DataBase.ConcreteLocalSource
import com.example.weather.Home.HomeViewModel.HomeViewModel
import com.example.weather.Home.HomeViewModel.HomeViewModelFactory
import com.example.weather.Model.Daily
import com.example.weather.Network.Weather_Client
import com.example.weather.R
import com.example.weather.Repo.Repo
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationView

import android.Manifest
import android.annotation.SuppressLint
import com.example.weather.Model.Hourly


class Home_View : AppCompatActivity() {

    // ______________ xml Component ______________
    var drawerLayout: DrawerLayout? = null
    var navigationView: NavigationView? = null

    var location: TextView? = null
    var description: TextView? = null
    var tempreture: TextView? = null
    var humidity: TextView? = null
    var pressure: TextView? = null
    var wind: TextView? = null
    var visiblity: TextView? = null
    var cloud: TextView? = null
    var ultraviolet: TextView? = null
    var weekRecycler: RecyclerView? = null
    var hourRecycler: RecyclerView? = null
    var weekAdapter = WeekRecyclerAdapter(this)
    var hourAdapter = HourRecyclerAdapter(this)

    //______________________________________
    lateinit var layoutManager: LinearLayoutManager

    //-------------------------
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var Latitude: Double =0.0
    var Longitude: Double =0.0
    var PERMISSION_ID = 45

    //-------------------

    lateinit var homeViewModel: HomeViewModel
    lateinit var myfactory: HomeViewModelFactory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        var intent = getIntent()

//        var lat = intent.getDoubleExtra("lon", 0.0)
        showSettingDialog()


        println(" after show setting function $Latitude , $Longitude")
        initComp()
    }

    //send to view model
    @SuppressLint("SetTextI18n")
    private fun putRequest() {
        myfactory = HomeViewModelFactory(
            Repo.getInstance(
                this,
                ConcreteLocalSource.getInstance(this),
                Weather_Client.getinstance()!!
            )
        )


        homeViewModel = ViewModelProvider(this, myfactory).get(HomeViewModel::class.java)

        homeViewModel.setLocation(
            long=Longitude,
            lat=Latitude
        )
        homeViewModel.getrequest()


        homeViewModel.dataimmutable.observe(this) { data ->
            if (data != null) {
                println(" current ${data.current}" )
                println(" daily  ${data.daily}" )
                println(" hourly ${data.hourly}" )
                println(" alerts ${data.alerts}" )
                println(" timezone ${data.timezone}")


                location?.text=data.timezone

                var weather= data.current?.weather?.get(0)
                description?.text=weather?.description

                tempreture?.text= data.current?.temp.toString()+"℃"

                humidity?.text=data.current?.humidity.toString()+"%"

                pressure?.text=data.current?.pressure.toString()+"hpa"

                wind?.text=data.current?.windSpeed.toString()+"m/s"

                ultraviolet?.text=data.current?.uvi.toString()+"m"

                cloud?.text=data.current?.clouds.toString()+"%"

                visiblity?.text=data.current?.visibility.toString()


                sethourRecycler(data.hourly)
                setWeekRecycler(data.daily)


            }
        }
    }

    private fun showSettingDialog() {
        val dialogBuilder = AlertDialog.Builder(this)

        var dialogView = LayoutInflater.from(this).inflate(R.layout.activity_main, null)
        var gps = dialogView.findViewById<RadioButton>(R.id.GPS_ID)
        var map = dialogView.findViewById<RadioButton>(R.id.map_ID)
        dialogBuilder.setPositiveButton("Done") { dialog, which ->

        }
//        dialogBuilder.setNegativeButton("CANCEL") { dialog, which -> }

        gps.setOnClickListener {

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

            getUserLocation()
        }
        map.setOnClickListener {
            println("map")
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
                        putRequest()

                        /*
                        *
                        * send to view model to get the data
                        * */


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

    private fun setWeekRecycler( daily: List<Daily>) {
//        var test = listOf<Daily>()
        layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = RecyclerView.VERTICAL
        weekAdapter.week = daily
        weekRecycler!!.adapter = weekAdapter
        weekRecycler!!.layoutManager = layoutManager
    }

    private fun sethourRecycler(hourly : List<Hourly>) {
        layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = RecyclerView.HORIZONTAL

        hourAdapter.hours=hourly
        hourRecycler!!.adapter = hourAdapter
        hourRecycler!!.layoutManager = layoutManager
    }


    //findbyid
    private fun initComp() {
        drawerLayout = findViewById(R.id.mydrawablebar)
        navigationView = findViewById(R.id.mynavigationbar)
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

        setMenu()
        setListners()
    }

    //nav_drawer
    private fun setMenu() {
        //keda el menu btatl3 mn el button
        // lw shilto yb2a h swap 3lashn ytal3
        //eli hoa el 3 short lw 3aiza anhom y3mlo functionality
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        //image bta3t el icon
        supportActionBar!!.setHomeAsUpIndicator(R.color.white)
    }

    //settings of navigation
    private fun setListners() {
        navigationView!!.setNavigationItemSelectedListener { menuItem: MenuItem ->

            //set item as selected to persist highlight
            menuItem.isChecked = true

            //close drawer when item is tapped
            drawerLayout!!.closeDrawers()
            true
        }


        drawerLayout!!.addDrawerListener(
            object : DrawerLayout.DrawerListener {
                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
                override fun onDrawerOpened(drawerView: View) {}
                override fun onDrawerClosed(drawerView: View) {}
                override fun onDrawerStateChanged(newState: Int) {}
            }
        )
    }

    //settings of navigation
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                drawerLayout!!.openDrawer(GravityCompat.START)
                return true
            }
        }

//        if (item.itemId == R.id.nav_home) {
//            Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
//            supportFragmentManager.beginTransaction().replace(
//                com.example.medicalreminder.home.view.Home.frameLayout.getId(),
//                HomeFragment()
//            ).commit()
        return super.onOptionsItemSelected(item)
    }


    //mobile permission
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
}
