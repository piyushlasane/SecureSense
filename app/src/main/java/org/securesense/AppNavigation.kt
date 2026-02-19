package org.securesense.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.securesense.screenpackage.HumidityDataScreen
import org.securesense.screens.AirQualityDataScreen
import org.securesense.screens.ConnectDeviceScreen
import org.securesense.screens.LightIntensityDataScreen
import org.securesense.screens.RainfallDataScreen
import org.securesense.screens.SensorReadingScreen
import org.securesense.screens.TemperatureDataScreen

object Routes {
    const val CONNECT_DEVICE    = "connect_device"     // ← new start screen
    const val SENSOR_READING    = "sensor_reading"
    const val TEMPERATURE       = "temperature"
    const val HUMIDITY          = "humidity"
    const val AIR_QUALITY       = "air_quality"
    const val LIGHT_INTENSITY   = "light_intensity"
    const val RAINFALL          = "rainfall"
}

@Composable
fun AppNavigation(paddingValues: PaddingValues) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.CONNECT_DEVICE        // ← starts here now
    ) {

        composable(Routes.CONNECT_DEVICE) {
            ConnectDeviceScreen(
                paddingValues = paddingValues,
                onConnectDevice = {
                    navController.navigate(Routes.SENSOR_READING) {
                        // Remove ConnectDevice from back stack so back button doesn't return to it
                        popUpTo(Routes.CONNECT_DEVICE) { inclusive = true }
                    }
                },
            )
        }

        composable(Routes.SENSOR_READING) {
            SensorReadingScreen(
                paddingValues = paddingValues,
                onNavigateToTemperature    = { navController.navigate(Routes.TEMPERATURE) },
                onNavigateToHumidity       = { navController.navigate(Routes.HUMIDITY) },
                onNavigateToAirQuality     = { navController.navigate(Routes.AIR_QUALITY) },
                onNavigateToLightIntensity = { navController.navigate(Routes.LIGHT_INTENSITY) },
                onNavigateToRainfall       = { navController.navigate(Routes.RAINFALL) }
            )
        }

        composable(Routes.TEMPERATURE) {
            TemperatureDataScreen(
                paddingValues = paddingValues,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.HUMIDITY) {
            HumidityDataScreen(
                paddingValues = paddingValues,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.AIR_QUALITY) {
            AirQualityDataScreen(
                paddingValues = paddingValues,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.LIGHT_INTENSITY) {
            LightIntensityDataScreen(
                paddingValues = paddingValues,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.RAINFALL) {
            RainfallDataScreen(
                paddingValues = paddingValues,
                onBack = { navController.popBackStack() }
            )
        }
    }
}