package com.example.app.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.app.R
import com.example.app.ui.components.ExampleAppBar
import com.example.app.ui.screens.DetailScreen
import com.example.app.ui.screens.HomeScreen
import com.example.app.viewModel.MainViewModel
import kotlinx.serialization.Serializable

@Composable
fun Navigation(
    navController: NavHostController = rememberNavController()
) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = backStackEntry.findCurrentRoute()

    Scaffold(
        topBar = {
            ExampleAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable<Screen.Home> {
                val viewModel: MainViewModel = hiltViewModel()
                HomeScreen(
                    viewModel = viewModel,
                    onItemTapped = { item ->
                        navController.navigate(Screen.Detail(item.media.imageUrl))
                    },
                    modifier = Modifier.fillMaxSize(),
                )
            }
            composable<Screen.Detail> { backStackEntry ->
                val screen: Screen.Detail = backStackEntry.toRoute()
                DetailScreen(
                    imageUrl = screen.imageUrl,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Serializable
open class Screen(@StringRes val title: Int) {

    @Serializable
    data object Home : Screen(R.string.screen_home)


    @Serializable
    data class Detail(
        val imageUrl: String
    ) : Screen(R.string.screen_detail)

}

fun NavBackStackEntry?.findCurrentRoute(): Screen {
    if (this == null) {
        return Screen.Home
    }
    return when (val destination = this.destination.route?.split('/')?.first()) {
        Screen.Home::class.qualifiedName -> toRoute<Screen.Home>()
        Screen.Detail::class.qualifiedName -> toRoute<Screen.Detail>()

        else -> throw IllegalArgumentException("Unknown route: $destination")
    }
}