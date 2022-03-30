package com.example.weather.Home.HomeView

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.Model.Pojo
import com.example.weather.R
import com.google.android.material.navigation.NavigationView

class Home_View : AppCompatActivity() {
    var drawerLayout: DrawerLayout? = null
    var navigationView: NavigationView? = null
    lateinit var layoutManager: LinearLayoutManager


    var location: TextView ?=null
    var description: TextView ?=null
    var tempreture: TextView ?=null
    var humidity: TextView ?=null
    var pressure: TextView ?=null
    var wind: TextView ?=null
    var visiblity: TextView ?=null
    var cloud: TextView ?=null
    var ultraviolet: TextView ?=null

    var weekRecycler : RecyclerView?=null
    var hourRecycler : RecyclerView?=null

    var weekAdapter= WeekRecyclerAdapter(this)
    var hourAdapter=HourRecyclerAdapter (this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        initComp()
        sethourRecycler()
        setWeekRecycler()



    }

    private fun setWeekRecycler() {
        var test = listOf<Pojo>(Pojo("sun"), Pojo("sun"),Pojo("sun"),Pojo("sun"),Pojo("sun"), Pojo("sun"),Pojo("sun"))
        layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = RecyclerView.VERTICAL
        weekAdapter.week=test
        weekRecycler!!.adapter=weekAdapter
        weekRecycler!!.layoutManager = layoutManager    }

    private fun sethourRecycler() {
        layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = RecyclerView.HORIZONTAL
        hourRecycler!!.adapter=hourAdapter
        hourRecycler!!.layoutManager = layoutManager
    }

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
        weekRecycler=findViewById(R.id.Week_Recycler)
        hourRecycler=findViewById(R.id.Hour_Recycler)

        setMenu()
        setListners()
    }

    private fun setMenu() {
        //keda el menu btatl3 mn el button
        // lw shilto yb2a h swap 3lashn ytal3
        //eli hoa el 3 short lw 3aiza anhom y3mlo functionality
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        //image bta3t el icon
        supportActionBar!!.setHomeAsUpIndicator(R.color.white)
    }

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
}