package com.example.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.example.utils.Common.showToast

class Permissions(private val context: Context) {
    fun checkPermission(): Boolean {
        val activity = context as Activity
        if (ContextCompat.checkSelfPermission(
                activity,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                activity,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (activity.shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION) && activity.shouldShowRequestPermissionRationale(
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                "ForeGround Location Permission Required".showToast(context)
                return true
            }
            return false
        }
        return true
    }

    fun checkPermission(permission:String,message:String): Boolean {
        val activity = context as Activity
        if (ContextCompat.checkSelfPermission(
                activity,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (activity.shouldShowRequestPermissionRationale(permission)
            ) {
                message.showToast(context)
                return true
            }
            return false
        }
        return true
    }
}