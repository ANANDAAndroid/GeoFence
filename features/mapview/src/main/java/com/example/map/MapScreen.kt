package com.example.map

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.CircularLoader
import com.example.utils.Common
import com.example.utils.Constants
import com.example.utils.R
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.serialization.Serializable


@Serializable
data object MapScreen

@SuppressLint("MissingPermission")
@Composable
fun MapScreen(
    context: Context = LocalContext.current,
    geofencing: Geofencing = Geofencing(LocalContext.current)
) {


    var isMapLoaded by remember { mutableStateOf(false) }
    var markerPosition by remember { mutableStateOf(LatLng(20.5937, 78.9629)) }
    var addGeoFence by remember { mutableStateOf(false) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(markerPosition, 15f)
    }

    val mapUiSettings by remember {
        mutableStateOf(MapUiSettings(myLocationButtonEnabled = true))
    }

    val mapProperties by remember {
        mutableStateOf(MapProperties(isMyLocationEnabled = true))
    }

    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    LaunchedEffect(true) {
        geofencing.checkLocationSettings()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapLoaded = { isMapLoaded = true },
            onMyLocationButtonClick = {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    val location = task.result
                    if (location != null) {
                        addGeoFence = false
                        markerPosition = LatLng(location.latitude, location.longitude)
                    }
                }
                false
            },
            onMapClick = {
                addGeoFence = false
                markerPosition = it
            },
            uiSettings = mapUiSettings,
            properties = mapProperties
        ) {
            MarkerComposable(
                state = MarkerState(position = markerPosition),
                keys = arrayOf(markerPosition)
            ) { MarkerDescription(markerPosition) }


            if (addGeoFence) {
                Circle(
                    center = markerPosition,
                    radius = Constants.GEOFENCE_RADIUS_IN_METERS.toDouble()
                )
            }
        }

        FloatingActionButton(
            onClick = {
                geofencing.addGeoFence(markerPosition)
                addGeoFence = true
            },
            shape = RoundedCornerShape(5.dp),
            containerColor = Color.Yellow,
            modifier = Modifier
                .padding(start = 20.dp, top = 20.dp)
                .height(30.dp)
        ) {
            Text(text = "Add Geofence", modifier = Modifier.padding(horizontal = 5.dp))
        }
        if (!isMapLoaded) {
            CircularLoader()
        }
    }
}

@Composable
private fun MarkerDescription(position: LatLng) {

    Column(
        verticalArrangement = Arrangement.spacedBy(0.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .background(shape = RoundedCornerShape(5.dp), color = Color.DarkGray)
                .padding(5.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Lat: ${Common.getFormatedLatLng(position.latitude)}",
                color = Color.White,
                fontSize = 11.sp,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )
            Text(
                text = "Lng: ${Common.getFormatedLatLng(position.longitude)}",
                color = Color.White,
                fontSize = 11.sp,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )
        }
        Icon(
            modifier = Modifier
                .size(30.dp)
                .clip(shape = RectangleShape),
            painter = painterResource(R.drawable.location),
            contentDescription = "icon",
            tint = Color.Red
        )
    }

}