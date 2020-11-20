package com.lib.filebrowserlibrary.old.ui.utils

import android.content.Context
import android.location.LocationManager
import android.net.ConnectivityManager

class NetworkUtils {
    companion object{
        fun isGpsEnable(context: Context): Boolean {
            val manager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return manager != null && manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        }

        fun isNetworkConnected(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting
        }
    }
}