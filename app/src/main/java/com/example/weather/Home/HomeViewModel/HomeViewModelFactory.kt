package com.example.weather.Home.HomeViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weather.Repo.Repo

class HomeViewModelFactory(private var repo: Repo) : ViewModelProvider.Factory {
    /*
    * first when i get the location from the gps send it to the home view model to show the temp
    *
    *
    * second when the user save the location save it in shared pref or repo and get it
    *
    * to send the location to the viewmodel to get the temp -- > immediately when the app opened
    * */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java))
                {
                HomeViewModel(repo) as T
                }
                else{
                    throw IllegalAccessException("not Found")
                }

    }


}