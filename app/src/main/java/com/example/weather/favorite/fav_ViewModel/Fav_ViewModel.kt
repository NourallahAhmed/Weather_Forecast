package com.example.weather.favorite.fav_ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.DataBase.Local_Source
import com.example.weather.FavModel.FavModel
import com.example.weather.Model.WeatherModel
import com.example.weather.Repo.Repo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Fav_ViewModel(var repo: Repo) : ViewModel() {

    //for network
    private val dataMutable = MutableLiveData<WeatherModel>()
    var dataimmutable : LiveData<WeatherModel> =dataMutable

    //for local fav
    private val dataFavMutable = MutableLiveData<List<FavModel>>()
    var dataFavimmutable : LiveData<List<FavModel>> =dataFavMutable



    //________________ get from api________________
    //_____________return WeatherModel_____________
    fun sendRequest(lat :Double , long :Double ,unit : String,lang : String){
        println("enter the send request in the fav ")
        viewModelScope.launch {
            val result=repo.getNetworkData(lat = lat , lon = long , unit = unit, lang = lang)
            withContext(Dispatchers.Main){
                dataMutable.postValue(result)
            }
        }

    }

    //_______________ get  fav from local_______________

    fun getFavLocal(){

        viewModelScope.launch {
            val result=repo.getFavData()
            withContext(Dispatchers.Main){
                dataFavMutable.postValue(result)
                println("from viewmodel $result")
            }
        }
    }


    //______________ insert fav  to local____________

    fun insertfav(model: FavModel) {

        println("in insert Fav")
        viewModelScope.launch {
            repo.insertToFav(model) }
    }

    //______________ delete fav from local_____________
    fun deletefav(model:FavModel){
        viewModelScope.launch {  repo.deleteFav(model)
        }

    }


}