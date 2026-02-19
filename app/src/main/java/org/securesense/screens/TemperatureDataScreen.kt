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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ─── Colors ──────────────────────────────────────────────────────────────────
private val BgBlue    = Color(0xFF93C6E7)
private val CardWhite = Color(0xFFF0F4FF)
private val ChartBlue = Color(0xFF3B6FD4)
private val TextDark  = Color(0xFF1A1D2E)
private val TextGray  = Color(0xFF9A9EB5)

// ─── Data ────────────────────────────────────────────────────────────────────
data class TemperatureHourData(
    val hour: String,
    val icon: String,
    val tempF: String
)

val temperatureHourlyData = listOf(
    TemperatureHourData("9AM",  "☀️", "68°"),
    TemperatureHourData("10AM", "☀️", "70°"),
    TemperatureHourData("11AM", "☀️", "72°"),
    TemperatureHourData("12PM", "☁️", "75°"),
    TemperatureHourData("1PM",  "☁️", "76°"),
    TemperatureHourData("2PM",  "🌧️", "74°"),
    TemperatureHourData("3PM",  "🌧️", "72°"),
    TemperatureHourData("4PM",  "☁️", "70°"),
    TemperatureHourData("5PM",  "☀️", "68°"),
)

// ─── Screen ──────────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemperatureDataScreen(
    onBack: () -> Unit = {},
    onSeeDetails: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgBlue)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ── Top bar ──
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextDark)
                }
                Text(
                    text = "Temperature Data",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextDark,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.width(48.dp)) // balance back button
            }

            Spacer(Modifier.height(20.dp))

            // ── Card ──
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        text = "Upcoming Hours",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )

                    Spacer(Modifier.height(16.dp))

                    // ── Weather icon + hour labels row ──
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        temperatureHourlyData.forEach { entry ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.width(50.dp)
                            ) {
                                Text(entry.icon, fontSize = 20.sp, textAlign = TextAlign.Center)
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = entry.hour,
                                    fontSize = 10.sp,
                                    color = TextGray,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    // ── CHART PLACEHOLDER ──
                    // Replace this Box with your Vico / MPAndroidChart composable
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .background(
                                color = Color(0xFFE8EEF8),
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "📈  Chart goes here",
                            fontSize = 14.sp,
                            color = ChartBlue,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    // ── Temperature values row ──
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        temperatureHourlyData.forEach { entry ->
                            Text(
                                text = entry.tempF,
                                fontSize = 12.sp,
                                color = ChartBlue,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width(50.dp)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(28.dp))

            // ── See Details Button ──
            Button(
                onClick = onSeeDetails,
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .height(52.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = CardWhite)
            ) {
                Text(
                    text = "See details",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
            }
        }
    }
}
