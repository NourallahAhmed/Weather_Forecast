package com.example.weather.favorite.fav_ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weather.Repo.Repo

class Fav_ViewModelFactory(private var repo: Repo): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return if (modelClass.isAssignableFrom(Fav_ViewModel::class.java))
        {
            Fav_ViewModel(repo) as T
        }
        else{
            throw IllegalAccessException("not Found")
        }
    }
}