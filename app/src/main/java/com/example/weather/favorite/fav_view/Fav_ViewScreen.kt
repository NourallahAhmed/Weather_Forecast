package com.example.weather.favorite.fav_view

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.weather.DataBase.ConcreteLocalSource
import com.example.weather.Home.HomeView.HomeFragment
import com.example.weather.Home.HomeView.HourRecyclerAdapter
import com.example.weather.Home.HomeView.WeekRecyclerAdapter
import com.example.weather.Home.HomeViewModel.HomeViewModel
import com.example.weather.Home.HomeViewModel.HomeViewModelFactory
import com.example.weather.Model.Daily
import com.example.weather.Model.Hourly
import com.example.weather.Model.WeatherModel
import com.example.weather.Network.Weather_Client
import com.example.weather.R
import com.example.weather.Repo.Repo
import com.example.weather.favorite.fav_ViewModel.Fav_ViewModel
import com.example.weather.favorite.fav_ViewModel.Fav_ViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import java.text.SimpleDateFormat
import java.util.*

class Fav_ViewScreen(var Lat: Double, var Long: Double) :Fragment() {
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

    var Latitude=Lat
    var Longitude=Long
    lateinit var favviewmodel:Fav_ViewModel
    lateinit var favviewfactory:Fav_ViewModelFactory

    lateinit var layoutManager: LinearLayoutManager

    //for language settings
    lateinit var locale: Locale
    var lang = true //true -> English
    var check=true //true -> first time
    var check2=false //false -> dont  refrech


    //for shared preferences
    lateinit var settings : SharedPreferences
    var langPref  : String?=null
    var tempunit :String ?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home,container,false)
    }


    @SuppressLint("UseRequireInsteadOfGet")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var context = view.context
        //first : factory and instance of fav_view_model
        favviewfactory= Fav_ViewModelFactory(Repo.getInstance( requireContext(), ConcreteLocalSource(requireContext() ), Weather_Client.getinstance()!!))
        favviewmodel= ViewModelProvider(this, favviewfactory).get(Fav_ViewModel::class.java)

        println("on Screen View $Latitude , $Longitude")
        initComp(view)



        //__________________________getting shared pref__________________________________

        /*
        * use shared pref to get the settings -> lang , units
        *
        * update the shared pref from settings page and receive it here
        *
        * */

        // if not exist will create one
        settings = requireContext().getSharedPreferences(HomeFragment.PREFS_NAME,0)
        // Latitude = settings.getString("map_lat", "0.0")?.toDouble() ?: 0.0
        // Longitude = settings.getString("map_long","0.0")?.toDouble() ?: 0.0
        //check=settings.getBoolean("check",true)
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
        println("fromsharedpref dialog = $check")
        println("fromsharedpref long = $Longitude")
        println("fromsharedpref lat = $Latitude")
        println("fromsharedpref lang = $lang")
        println("fromsharedpref temp = $tempunit")

        check2=requireActivity().intent.getBooleanExtra("refresh",false)

        //__________________________________HOMESETTINGS______________________________________

        if(check2==true){ languageSettings() }
        //_________________________________SendTOAPI_________________________________
        favviewmodel.sendRequest(Latitude,long=Longitude,lang =langPref!! ,unit = tempunit!!  )

        favviewmodel.dataimmutable.observe(requireActivity()) { data ->
            if (data != null) {
                setDataOnHomeScreen(data)
            }

        }

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

}