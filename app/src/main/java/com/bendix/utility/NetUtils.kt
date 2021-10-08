package com.bendix.utility

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import java.lang.Exception

class NetUtils {
    companion object {
        //Check for connectivity
        fun isNetworkAvailable(context: Context): Boolean {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    var isOnline = false
                    try {
                        val manager =
                            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                        // need ACCESS_NETWORK_STATE permission
                        val capabilities = manager.getNetworkCapabilities(manager.activeNetwork)
                        isOnline =
                            capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    return isOnline
                } else {
                    val cm =
                        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                    val netInfo = cm.activeNetworkInfo
                    if (netInfo != null && netInfo.isConnected) return true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }
    }
}