package com.example.userauthentication

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.utils.Permissions
import kotlinx.serialization.Serializable

@Serializable
data object LogInScreen

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun LogInScreen(
    permissions: Permissions = Permissions(LocalContext.current),
    onClick: () -> Unit = {}
) {

    val listOfForeGroundLocation = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )


    val backGroundPermission =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { _ -> }
    val notificationPermission =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { _ -> }

    val foreGroundPermission =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            if (result[listOfForeGroundLocation[0]] == true && result[listOfForeGroundLocation[1]] == true) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (!permissions.checkPermission(
                            android.Manifest.permission.POST_NOTIFICATIONS,
                            "Permission Required"
                        )
                    )
                        notificationPermission.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }


    LaunchedEffect(key1 = true) {
        if (!permissions.checkPermission()) {
            foreGroundPermission.launch(listOfForeGroundLocation)
        }
    }


    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Green)
    ) {

        val (tvLogin, utUsername, utPassword, button, backGroundLocation) = createRefs()

        Text(
            modifier = Modifier.constrainAs(tvLogin) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom, margin = 200.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }, text = "LogIn to Map",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .constrainAs(utUsername) {
                    top.linkTo(tvLogin.bottom, margin = 10.dp)
                },
            label = { Text("Username") },
            value = username,
            onValueChange = { username = it }
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .constrainAs(utPassword) {
                    top.linkTo(utUsername.bottom, margin = 10.dp)
                },
            label = { Text("Password") },
            value = password,
            onValueChange = { password = it }
        )
        Button(onClick = {
            onClick.invoke()
        }, modifier = Modifier.constrainAs(button) {
            top.linkTo(utPassword.bottom, margin = 10.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }) {
            Text(text = "Login", fontSize = 18.sp, modifier = Modifier.padding(horizontal = 20.dp))
        }

        Button(onClick = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                backGroundPermission.launch(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            }

        }, modifier = Modifier.constrainAs(backGroundLocation) {
            top.linkTo(button.bottom, margin = 10.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }) {
            Text(
                text = "Enable Background Location",
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }
    }
}



