package com.example.weather

import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.weather.Home.HomeView.HomeFragment
import com.example.weather.Home.HomeView.Home_View
import com.example.weather.databinding.ActivityMapsBinding
import com.example.weather.favorite.fav_view.Fav_View
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import java.util.*

class MapFragment :Fragment() , OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    var currentMarker : Marker?=null
    lateinit var address:String
    //for shared preferences
    lateinit var settings : SharedPreferences
    lateinit var editor : SharedPreferences.Editor
    val PREFS_NAME= "mysharedfile"
    var vi : View? = null
    var fusedLocationProviderClient : FusedLocationProviderClient?=null
    var currentlocation : Location?=null

    var sendLoc :String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return   inflater.inflate(R.layout.activity_maps,container,false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        settings = requireActivity().getSharedPreferences(PREFS_NAME,0)
        editor = settings.edit()
        vi=view
        //receiving the intent

//        incoming_intent= requireActivity().intent.getStringExtra("fragment")!!

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fetchLoaction()
    }


    private fun fetchLoaction() {
    //runtime permission
    if(ActivityCompat.checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_FINE_LOCATION)
    != PackageManager.PERMISSION_GRANTED &&
    ActivityCompat.checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
    PackageManager.PERMISSION_GRANTED){

        //send request to the user to use these permissions
        ActivityCompat.requestPermissions(requireActivity(),arrayOf (android.Manifest.permission.ACCESS_FINE_LOCATION ),1000)
        return
    }
    val task = fusedLocationProviderClient?.lastLocation
    task?.addOnSuccessListener {
        if( it != null){
            this.currentlocation=it

//            val mapFragment = requireActivity().supportFragmentManager
//                .findFragmentById(R.id.map) as SupportMapFragment
//                mapFragment.getMapAsync(this)
            val mp = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
            mp.getMapAsync(this)

        }
    }
}

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            1000 -> if(grantResults.size>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                fetchLoaction()
            }
        }
    }

/**
 * Manipulates the map once available.
 * This callback is triggered when the map is ready to be used.
 * This is where we can add markers or lines, add listeners or move the camera. In this case,
 * we just add a marker near Sydney, Australia.
 * If Google Play services is not installed on the device, the user will be prompted to install
 * it inside the SupportMapFragment. This method will only be triggered once the user has
 * installed Google Play services and returned to the app.
 */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val latlong= LatLng(currentlocation?.latitude!!,currentlocation?.longitude!!)

        drawMarker(latlong)

        //drag the marker
        mMap.setOnMarkerDragListener(object: GoogleMap.OnMarkerDragListener{
            override fun onMarkerDrag(p0: Marker) {
            }

            override fun onMarkerDragEnd(p0: Marker) {
                if(currentMarker!=null){
                    //remove the previous marker and add new one
                    currentMarker?.remove()
                    val newLatlong= LatLng(p0?.position!!.latitude,p0?.position!!.longitude)
                    drawMarker(newLatlong)




                    Snackbar.make(vi!!, "This is your Location?", Snackbar.LENGTH_LONG)
                        .setAction("Save", object : View.OnClickListener {
                            override fun onClick(view: View?) {
                                val mFragmentManager = requireActivity().supportFragmentManager
                                val mFragmentTransaction = mFragmentManager.beginTransaction()

                                var newfr =mFragmentManager.findFragmentByTag("fav")
                                if(newfr!=null){
                                    // Declaring fragment manager from making data
//                                     transactions using the custom fragment
                                    val mFragment = Fav_View()
                                    mFragmentTransaction.replace(MainActivity2.x, mFragment).commit()

                                    Fav_View.Lat=newLatlong.latitude
                                    Fav_View.Long=newLatlong.longitude
                                    Fav_View.Loc=sendLoc
                                    println("From Fragment Map Loc  ${sendLoc}")

                                    println("From Fragment Map long ${newLatlong.longitude}")
                                    println("From Fragment Map lat ${newLatlong.latitude}")
                                }

                                else{
                                    editor.putString("map_lat", newLatlong.latitude.toString())
                                    editor.putString("map_long",newLatlong.longitude.toString())

                                    //do not show the dialog
                                    editor.putBoolean("check", false)
                                    println("Map long ${newLatlong.longitude}")
                                    println("Map lat ${newLatlong.latitude}")
                                        val mFragment = HomeFragment()
                                        mFragmentTransaction.replace(MainActivity2.x, mFragment).commit()
                                }
                            }
                        })
                        .setActionTextColor(resources.getColor(android.R.color.holo_red_light))
                        .show()


                }
            }
            override fun onMarkerDragStart(p0: Marker) {
            }
        })
    }
    private fun drawMarker(latlong: LatLng) {
        val marker = MarkerOptions().position(latlong).title("I am here")
            .snippet(getAddress(latlong.latitude,latlong.longitude)).draggable(true) //  to be able to dragged

        mMap.animateCamera(CameraUpdateFactory.newLatLng(latlong))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlong,15f))

        currentMarker = mMap.addMarker(marker)
        currentMarker?.showInfoWindow()



    }

    private fun getAddress(latitude: Double, longitude: Double): String? {
        val geocoder= Geocoder(requireContext(), Locale.getDefault())
        //list of addresses
        val addresses = geocoder.getFromLocation(latitude,longitude,1)

        address=addresses[0].getAddressLine(0).toString()
        sendLoc=address
        println("the address $address")
        return address
    }


    override fun onPause() {
        super.onPause()
        editor.commit()
    }

}