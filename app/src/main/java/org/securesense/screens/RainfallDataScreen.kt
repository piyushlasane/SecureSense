package org.securesense.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val BgBlue    = Color(0xFF93C6E7)
private val CardWhite = Color(0xFFF0F4FF)
private val ChartBlue = Color(0xFF3B6FD4)
private val TextDark  = Color(0xFF1A1D2E)
private val TextGray  = Color(0xFF9A9EB5)

data class RainfallHourData(val hour: String, val icon: String, val rainfall: String)

val rainfallHourlyData = listOf(
    RainfallHourData("9AM",  "☀️", "10%"), RainfallHourData("10AM", "☀️", "15%"),
    RainfallHourData("11AM", "☀️", "20%"), RainfallHourData("12PM", "☁️", "35%"),
    RainfallHourData("1PM",  "☁️", "45%"), RainfallHourData("2PM",  "🌧️", "60%"),
    RainfallHourData("3PM",  "🌧️", "70%"), RainfallHourData("4PM",  "☁️", "50%"),
    RainfallHourData("5PM",  "☀️", "30%"),
)

@Composable
fun RainfallDataScreen(
    paddingValues: PaddingValues,
    onBack: () -> Unit = {},
    onSeeDetails: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgBlue)
            .padding(paddingValues)                          // ← Scaffold insets
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextDark)
            }
            Text(text = "Rainfall Data", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = TextDark, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
            Spacer(Modifier.width(48.dp))
        }

        Spacer(Modifier.height(20.dp))

        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = CardWhite), elevation = CardDefaults.cardElevation(0.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Upcoming Hours", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextDark)
                Spacer(Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.SpaceBetween) {
                    rainfallHourlyData.forEach { entry ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(50.dp)) {
                            Text(entry.icon, fontSize = 20.sp, textAlign = TextAlign.Center)
                            Spacer(Modifier.height(4.dp))
                            Text(entry.hour, fontSize = 10.sp, color = TextGray, textAlign = TextAlign.Center)
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                Box(modifier = Modifier.fillMaxWidth().height(220.dp).background(Color(0xFFE8EEF8), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                    Text("📈  Chart goes here", fontSize = 14.sp, color = ChartBlue, fontWeight = FontWeight.Medium)
                }

                Spacer(Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.SpaceBetween) {
                    rainfallHourlyData.forEach { entry ->
                        Text(text = entry.rainfall, fontSize = 12.sp, color = ChartBlue, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center, modifier = Modifier.width(50.dp))
                    }
                }
            }
        }

        Spacer(Modifier.height(28.dp))

        Button(onClick = onSeeDetails, modifier = Modifier.fillMaxWidth(0.75f).height(52.dp), shape = RoundedCornerShape(50), colors = ButtonDefaults.buttonColors(containerColor = CardWhite)) {
            Text("See details", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextDark)
        }
    }
}