package com.example.weather.Settings.SettingsViewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weather.Repo.Repo

class SettingsViewModelFactory(private var repo: Repo) : ViewModelProvider.Factory  {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SettingsViewModel::class.java))
        {
            SettingsViewModel(repo) as T
        }
        else{
            throw IllegalAccessException("not Found")
        }

    }
}