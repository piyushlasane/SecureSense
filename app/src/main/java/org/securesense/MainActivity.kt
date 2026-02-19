package org.securesense

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.securesense.screenpackage.HumidityDataScreen
import org.securesense.screens.AirQualityDataScreen
import org.securesense.screens.LightIntensityDataScreen
import org.securesense.screens.SensorReadingScreen
import org.securesense.ui.theme.SecureSenseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SecureSenseTheme {
                LightIntensityDataScreen()
            }
        }
    }
}
