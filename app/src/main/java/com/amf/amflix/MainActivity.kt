package com.amf.amflix

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.amf.amflix.common.NetworkReceiver
import com.amf.amflix.common.NetworkUtils
import com.amf.amflix.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * En esta clase, la principal, hay una vista oculta, que se pondrá en funcionamiento
 * próximamente para un modo Admin.
 */

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var networkReceiver: NetworkReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_nav_animation)

        val navView: BottomNavigationView = binding.navView
        navView.startAnimation(navAnimation)

        supportActionBar?.hide()

        navController = findNavController(R.id.nav_host_fragment_activity_main)

        if (!NetworkUtils.isConnectedToInternet(this)) {
            navController.navigate(R.id.navigation_connection)
        } else {
            navController.navigate(R.id.navigation_home)
        }

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_search,
                R.id.navigation_marcadores,
                R.id.navigation_settings,
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        networkReceiver = NetworkReceiver(navController)
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkReceiver, filter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(networkReceiver)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    /* No puedes pulsar atrás si estás en uno de estos fragmentos. */
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val dest = navController.currentDestination?.id

        if (dest == R.id.navigation_home ||
            dest == R.id.navigation_people ||
            dest == R.id.navigation_search ||
            dest == R.id.navigation_marcadores ||
            dest == R.id.navigation_settings) {

        } else {
            super.onBackPressed()
        }
    }

}
