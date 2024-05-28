package com.amf.amflix

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.amf.amflix.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_nav_animation)

        val navView: BottomNavigationView = binding.navView
        navView.startAnimation(navAnimation)

        supportActionBar?.hide()

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        navController.navigate(R.id.navigation_home)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_search,
                R.id.navigation_marcadores,
                R.id.navigation_people
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        /*navView.setOnNavigationItemSelectedListener { item ->
            // Manejar la selección de elementos del menú
            when (item.itemId) {
                R.id.navigation_dashboard -> {
                    // Navegar al dashboard solo si no estamos ya en él
                    if (navController.currentDestination?.id != R.id.navigation_dashboard) {
                        navController.navigate(R.id.navigation_dashboard)
                    }
                    true
                }
                else -> {
                    // Navegar a otros destinos
                    item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
                }
            }
        }*/
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val dest = navController.currentDestination?.id

        if (dest == R.id.navigation_home ||
            dest == R.id.navigation_series ||
            dest == R.id.navigation_people ||
            dest == R.id.navigation_search ||
            dest == R.id.navigation_marcadores) {

        } else {
            super.onBackPressed()
        }
    }

}
