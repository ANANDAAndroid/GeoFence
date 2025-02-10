package com.example.map

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.utils.Notification
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent

class GeofenceBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val geofencingEvent = intent?.let { GeofencingEvent.fromIntent(it) }
        if (geofencingEvent != null) {
            if (geofencingEvent.hasError()) {
                val errorMessage = GeofenceStatusCodes
                    .getStatusCodeString(geofencingEvent.errorCode)
                Log.e("geofence", errorMessage)
                return
            }
        }

        // Get the transition type.
        val geofenceTransition = geofencingEvent?.geofenceTransition
        val triggeringGeofence = geofencingEvent?.triggeringGeofences
        println("geofence triggering  $triggeringGeofence")
        println("geofence event  $geofencingEvent")
        when(geofenceTransition){
            Geofence.GEOFENCE_TRANSITION_ENTER -> {
                println("geofence zone in")
                context?.let { Notification(it) }?.sendNotification("Zone in", "You are with in area")

            }

            Geofence.GEOFENCE_TRANSITION_EXIT->{
                println("geofence zone out")
                context?.let { Notification(it) }?.sendNotification("Zone out","You are out of area")
            }
        }
    }
}