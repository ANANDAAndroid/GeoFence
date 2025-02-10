package com.example.map

import android.annotation.SuppressLint
import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.utils.Constants
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.maps.model.LatLng
import java.util.UUID

class Geofencing(private val context: Context) {
    private var geofenceList: ArrayList<Geofence> = arrayListOf()
    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
        PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    fun addGeoFence(latLong: LatLng) {
        geofenceList.clear()
        geofenceList.add(
            Geofence.Builder()
                .setRequestId(UUID.randomUUID().toString())
                .setCircularRegion(
                    latLong.latitude,
                    latLong.longitude,
                    Constants.GEOFENCE_RADIUS_IN_METERS
                )
                .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
                .build()
        )
        addGeoFencing()
    }

    @SuppressLint("MissingPermission")
    private fun addGeoFencing() {
        val geofencingClient = LocationServices.getGeofencingClient(context)
        geofencingClient.removeGeofences(geofencePendingIntent).run {
            addOnSuccessListener {
                println("geofence removed")
            }
            addOnFailureListener {
                println("geofence not removed ${it.message}")
            }
        }
        geofencingClient.addGeofences(getGeofencingRequest(), geofencePendingIntent).run {
            addOnSuccessListener {
                println("geofence added")
            }
            addOnFailureListener {
                println("geofence not added ${it.message}")
            }
        }
    }

    private fun getGeofencingRequest(): GeofencingRequest {
        return GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER or GeofencingRequest.INITIAL_TRIGGER_EXIT)
            addGeofences(geofenceList)
        }.build()
    }

    fun checkLocationSettings() {

        val locationRequest = LocationRequest.Builder(1000).build()
        val locationSettingsBuilder =
            LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
                .setAlwaysShow(true)


        val locationSettingsClient = LocationServices.getSettingsClient(context)
            .checkLocationSettings(locationSettingsBuilder.build())
        locationSettingsClient.addOnCompleteListener { task ->
            try {
                val response = task.getResult(ApiException::class.java)
                println("location settings result: ${response.locationSettingsStates}")
            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->

                        try {
                            val resolvable = exception as ResolvableApiException
                            resolvable.startResolutionForResult(
                                context as Activity,
                                200
                            )
                        } catch (_: Exception) {

                        }

                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
                }
            }


        }
    }
}