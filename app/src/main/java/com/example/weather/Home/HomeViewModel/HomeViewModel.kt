package com.example.weather.Home.HomeViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.Repo.Repo
import com.example.weather.Model.WeatherModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(var repo: Repo) :ViewModel() {

    //for network
    private val dataMutable =MutableLiveData<WeatherModel>()

    var dataimmutable : LiveData<WeatherModel> =dataMutable


    //insert after every update from the retrofit
    fun inserttoDB(Model: WeatherModel){
        viewModelScope.launch(Dispatchers.IO){
                 repo.insertData(Model) } }

    //get request from API ((NETWORK))
    fun getrequest(lang: String, unit: String, long: Double, lat: Double) {
        viewModelScope.launch {
            val result=repo.getNetworkData(lat = lat , lon = long , unit = unit, lang = lang)
            withContext(Dispatchers.Main){
                dataMutable.postValue(result)
            }
        }
    }

    fun getLocal(longitude: Double, latitude: Double) {
        viewModelScope.launch {
            val result=repo.getStoredData(longitude,latitude)
            withContext(Dispatchers.Main){
                dataMutable.postValue(result)
            }
        }
    }
}


/*
* checkButton.setOnClickListener {
            if (checkForInternet(this)) {
                Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Disconnected", Toast.LENGTH_SHORT).show()
            }
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
* */