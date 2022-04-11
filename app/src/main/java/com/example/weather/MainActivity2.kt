package com.example.weather

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.weather.Alert.AlertView.Alert_View

import com.example.weather.Home.HomeView.HomeFragment

import com.example.weather.Settings.SettingsView.SettingsFragment
import com.example.weather.favorite.fav_view.Fav_View
import com.google.android.material.navigation.NavigationView


class MainActivity2 : AppCompatActivity() {

    lateinit var frmgr: FragmentManager
    lateinit var transaction: FragmentTransaction
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var view: FrameLayout
    lateinit var toggle: ActionBarDrawerToggle
//    val transaction = supportFragmentManager.beginTransaction()

     var lastfragment : Fragment = HomeFragment()


    companion object{
        var x = R.id.navcontainer
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        drawerLayout=findViewById(R.id.drawerLayout)
        frmgr= supportFragmentManager
        transaction =supportFragmentManager.beginTransaction()
        transaction.replace(R.id.navcontainer,HomeFragment())
        transaction.commit()
        toggle= ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationView=findViewById(R.id.navView)
        navigationView.setNavigationItemSelectedListener {
            when(it.itemId){

                R.id.home_ID2 -> {
                    frmgr.beginTransaction().remove(lastfragment!!)
                    transaction =supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.navcontainer,HomeFragment())
                    transaction.commit()
                    lastfragment=HomeFragment()

                    drawerLayout.closeDrawers()
                }
                R.id.setting2 -> {
                    frmgr.beginTransaction().remove(lastfragment!!)
                    transaction =supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.navcontainer,SettingsFragment())
                    transaction.commit()
                    lastfragment=SettingsFragment()
                    drawerLayout.closeDrawers()

                }
                R.id.Fav_Id2 ->  {
                    frmgr.beginTransaction().remove(lastfragment!!)
                    transaction =supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.navcontainer,Fav_View())
                    transaction.commit()
                    lastfragment=Fav_View()
                    drawerLayout.closeDrawers()
                }
                R.id.alerts2 ->{
                    frmgr.beginTransaction().remove(lastfragment!!)
                    transaction =supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.navcontainer,Alert_View())
                    transaction.commit()
                    lastfragment=Alert_View()
                    drawerLayout.closeDrawers()
                }

            }
            true
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}




/*
* //--------------------------------- Start of action bar methods -------------------------------------------------------
    private void setActionBar(){
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        // methods to display the icon in the ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_account_circle_24);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("");
        setListeners();
    }
    private void setListeners() {
        navigationView.setNavigationItemSelectedListener(
                (new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        drawerLayout.closeDrawers();
                        Fragment dfragment = null;
                        if (menuItem.toString().equals("Add Dependent")) {
                            dfragment = new AddDependentFragment();
                            Intent intent = new Intent(HomeActivity.this, DependentActivity.class);
                            startActivity(intent);

                        } else if (menuItem.toString().equals("Invite Medfriend")) {
                            Intent intent = new Intent(HomeActivity.this, MedfriendActivity.class);
                            startActivity(intent);
                        } else if (menuItem.toString().equals("Requests")){
                            startActivity(new Intent(HomeActivity.this , RequestsActivity.class)); // senderRequestsActivity
                        }else if(menuItem.toString().equals("Logout")) {
                            presenterInterface.logout();
                            startActivity(new Intent(HomeActivity.this, LoginScreenActivity.class));
                        }
                        return true;
                    }
                }));
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {}

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {}

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {}

            @Override
            public void onDrawerStateChanged(int newState) {}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tool_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.notification_icon:
                Toast.makeText(this, "notification", Toast.LENGTH_SHORT).show();
                return true;

            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        Toast.makeText(this, "no item", Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }
    //--------------------------------- End of action bar methods -------------------------------------------------------
* */
//---------------> Button Bar <--------------
//        //Initialize the bottom navigation view
//        //create bottom navigation view object
//        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigatin_view)
//        val navController = findNavController(R.id.nav_fragment)
//        BottomNavigationView.OnNavigationItemSelectedListener { item ->
//            when(item.itemId) {
//                R.id.home_ID2 -> {
//                    // Respond to navigation item 1 click
//                    startActivity(Intent(this@MainActivity2, Home_View::class.java))
//                    true
//                }
//                R.id.setting2 -> {
//                    // Respond to navigation item 2 click
//                    startActivity(Intent(this@MainActivity2, SettingsActivity::class.java))
//
//                    true
//                }
//                else -> false
//            }
//        }
//        bottomNavigationView.setupWithNavController(navController)