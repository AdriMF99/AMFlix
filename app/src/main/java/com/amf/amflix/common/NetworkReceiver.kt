package com.amf.amflix.common

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.navigation.NavController
import com.amf.amflix.R

class NetworkReceiver(private val navController: NavController) : BroadcastReceiver() {
    private var previousDestination: Int? = null

    override fun onReceive(context: Context, intent: Intent) {
        if (isConnectedToInternet(context)) {
            previousDestination?.let {
                navController.navigate(it)
            } ?: run {
                navController.navigate(R.id.navigation_home)
            }
        } else {
            previousDestination = navController.currentDestination?.id
            navController.navigate(R.id.navigation_connection)
        }
    }

    private fun isConnectedToInternet(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }
}