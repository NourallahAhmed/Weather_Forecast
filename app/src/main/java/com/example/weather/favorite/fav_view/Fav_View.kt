package com.example.weather.favorite.fav_view

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.DataBase.ConcreteLocalSource
import com.example.weather.FavModel.FavModel
import com.example.weather.MainActivity2
import com.example.weather.MapFragment
import com.example.weather.Model.WeatherModel
import com.example.weather.Network.Weather_Client
import com.example.weather.R
import com.example.weather.Repo.Repo
import com.example.weather.favorite.fav_ViewModel.Fav_ViewModel
import com.example.weather.favorite.fav_ViewModel.Fav_ViewModelFactory
import com.example.weather.favorite.fav_ViewModel.OnFavClicked


class Fav_View : Fragment() , OnFavClicked {
    lateinit var fav_Recycler :RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    lateinit var FAVadapter: Fav_Adapter
    lateinit var addfav : Button
    lateinit var favviewmodel:Fav_ViewModel
    lateinit var favviewfactory:Fav_ViewModelFactory


    var country:String ?= null
    var Fav_countries: List<FavModel> =  listOf()

    companion object{
        var Lat:Double=0.0
        var Long:Double=0.0
        var Loc:String?=null

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fav_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fav_Recycler =view.findViewById(R.id.fav_Recycler)
        FAVadapter= Fav_Adapter(this)
        fav_Recycler.adapter= FAVadapter

        //first : factory and instance of fav_view_model
        favviewfactory=Fav_ViewModelFactory(Repo.getInstance( requireContext(), ConcreteLocalSource(requireContext() ), Weather_Client.getinstance()!!))
        favviewmodel= ViewModelProvider(this, favviewfactory).get(Fav_ViewModel::class.java)

        //second : get the data from  DB if exist and assign the result in the Fav_countries

        favviewmodel.getFavLocal()
        favviewmodel.dataFavimmutable.observe(viewLifecycleOwner){
            Fav_countries=it
            setFavRecycler(it)
            println("onViewCreated $Fav_countries")
        }


        //third : update the Recycler



        //forth : add fav button
        addfav=view.findViewById(R.id.addfav)

        addfav.setOnClickListener {

            if(checkForInternet(requireContext())){
                val mFragmentManager = requireActivity().supportFragmentManager
                val mFragmentTransaction = mFragmentManager.beginTransaction()
                val mFragment = MapFragment()
                val bundle=Bundle()
                bundle.putString("fragment","fav")
                mFragmentManager?.beginTransaction()?.replace(MainActivity2.x, mFragment,"fav").commit();
            }
            else{
                Toast.makeText(requireContext(),R.string.checkinternet , Toast.LENGTH_LONG).show()
            }


        }

        //fifth : get the lat and long and out them in model and add to the list FAVCountries
        // Gets the data from the passed bundle
        if(Lat!=0.0 && Long != 0.0){
            println("after map $Long  +  $Lat")
            sendRequestToApi(Lat, Long , Loc)
        }


//        //six : store in Model
//        var favc= FavModel(Lat!!,Long!!,country)
//
//        CoroutineScope(Dispatchers.IO).launch {
//            favviewmodel.insertfav(favc)
//        }
    }

    private fun sendRequestToApi(lat: Double, long: Double, Loc: String?) {
        //send to Api
        favviewmodel.sendRequest(Lat!!,Long!!,"standard","en")

        // insert to DB
        var fav = FavModel(Lat, Long,Loc)
        favviewmodel.insertfav(fav)

        favviewmodel.getFavLocal()
        favviewmodel.dataFavimmutable.observe(viewLifecycleOwner){
            println("from Fav ${it}");
            setFavRecycler(it)
        }

    }

    private fun setFavRecycler(Fav_countries: List<FavModel>) {
        println("set $Fav_countries")
        layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = RecyclerView.VERTICAL
        fav_Recycler!!.layoutManager = layoutManager
        fav_Recycler?.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                layoutManager.orientation
            )
        )
        FAVadapter.favCountries = Fav_countries
        FAVadapter.notifyDataSetChanged()
    }

   //showing the city
    override fun onMovieCLicked(lat: Double, lon: Double) {
//  check internet first
       if(checkForInternet(requireContext())){
       val mFragment = Fav_ViewScreen(lat, lon )
        println("from view ${lat} , ${lon}")
        getFragmentManager()?.beginTransaction()?.
        replace(MainActivity2.x , mFragment)
            ?.commit();}
       else{
           Toast.makeText(requireContext(),R.string.checkinternet , Toast.LENGTH_LONG).show()
       }
    }


    override fun onMovieDeleted(model: FavModel) {
        //Delete
        favviewmodel.deletefav(model)
        favviewmodel.getFavLocal()
        favviewmodel.dataFavimmutable.observe(viewLifecycleOwner){
            println("from Fav ${it}");
            setFavRecycler(it)
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