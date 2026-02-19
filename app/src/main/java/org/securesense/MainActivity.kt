package org.securesense

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import org.securesense.navigation.AppNavigation
import org.securesense.ui.theme.SecureSenseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SecureSenseTheme {
                Scaffold { paddingValues ->
                    AppNavigation(paddingValues = paddingValues)
                }
            }
        }
    }
}