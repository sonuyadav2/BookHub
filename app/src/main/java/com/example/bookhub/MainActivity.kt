package com.example.bookhub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationBarItemView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    ////////////////////////////////////must remember to give the name in proper manner///////////////
    lateinit var drawerlayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var navigationview: NavigationView
    lateinit var frameLayout: FrameLayout

    var previousMenuItem: MenuItem?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawerlayout = findViewById(R.id.drawerlayout)
        coordinatorLayout = findViewById(R.id.coordinatorlayout)
        toolbar = findViewById(R.id.toolbar)
        navigationview = findViewById(R.id.navigationview)
        frameLayout = findViewById(R.id.framelayout)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "BOOK HUB"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
/////////////////////////////////////////// to bring hameburger icon ////////////////
        val hlo = ActionBarDrawerToggle(
            this@MainActivity,
            drawerlayout,
            R.string.open_drawer,
            R.string.close_drawer
        )

        drawerlayout.addDrawerListener(hlo)
        hlo.syncState();
/////////////////////////////////////////////////////to start the page with dash board///////////
        openDashboard()
//////////////////////////////////////////////////////////////to use navigation bar//////////
        navigationview.setNavigationItemSelectedListener {

            /////////////// to highlight to tap view of navigation button////////////////////
            if (previousMenuItem != null) {
                previousMenuItem?.isChecked = false
            }
            it.isCheckable = true
            it.isChecked = true
            previousMenuItem = it
            when (it.itemId) {
                R.id.dashboard -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.framelayout, DashboardFragment()).commit()
                    drawerlayout.closeDrawers()
                    supportActionBar?.title = "Dashboard"
                }
                R.id.favourites -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.framelayout, FavouritesFragment()).commit()
                    drawerlayout.closeDrawers()
                    supportActionBar?.title = "Favourites"
                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.framelayout, ProfileFragment()).commit()
                    drawerlayout.closeDrawers()
                    supportActionBar?.title = "Profile"
                }
                R.id.aboutUs -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.framelayout, AboutUsFragment()).commit()
                    drawerlayout.closeDrawers()
                    supportActionBar?.title = "About Us"
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }

    ////////////////////////////////////when we click on the hame burger then its come from left or right//////////////////
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            drawerlayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    //////////////////////////////////////////////////// open dash bord function as it is used many times thats why me made it//////////
    fun openDashboard() {
        val fag = DashboardFragment
        var transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.framelayout, DashboardFragment())
        transaction.commit()
        drawerlayout.closeDrawers()
        supportActionBar?.title = "Dashboard"
        navigationview.setCheckedItem(R.id.dashboard)
    }

    /////////////////////////////////////// on back press what happen ////////////////////////////
    override fun onBackPressed() {
        var fram = supportFragmentManager.findFragmentById(R.id.framelayout)
        when (fram) {
            !is DashboardFragment -> openDashboard()
            else -> super.onBackPressed()
        }

    }
}