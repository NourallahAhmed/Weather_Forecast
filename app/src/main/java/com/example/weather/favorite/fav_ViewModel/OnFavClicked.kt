package com.example.weather.favorite.fav_ViewModel

import com.example.weather.FavModel.FavModel


interface OnFavClicked {
    fun onMovieCLicked(model: Double, lon: Double)
    fun onMovieDeleted(model:FavModel)
}