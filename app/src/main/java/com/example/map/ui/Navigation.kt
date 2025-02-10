package com.example.map.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.map.MapScreen
import com.example.userauthentication.LogInScreen

@Composable
fun MainNavigation() {
    val navHostController = rememberNavController()

    NavHost(navController = navHostController, startDestination = LogInScreen,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(700)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(700)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(700)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(700)
            )
        }) {
        composable<LogInScreen> {
            LogInScreen {
                navHostController.navigate(MapScreen)
            }
        }

        composable<MapScreen> {
            MapScreen()
        }
    }
}