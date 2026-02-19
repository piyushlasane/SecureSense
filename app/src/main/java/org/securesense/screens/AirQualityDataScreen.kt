package org.securesense.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val BgBlue      = Color(0xFF93C6E7)
private val CardWhite   = Color(0xFFF0F4FF)
private val ChartOrange = Color(0xFFFF9800)
private val TextDark    = Color(0xFF1A1D2E)
private val TextGray    = Color(0xFF9A9EB5)

data class AqiDayData(
    val day: String,
    val aqi: Int
)

val aqiWeeklyData = listOf(
    AqiDayData("Mon", 95),
    AqiDayData("Tue", 110),
    AqiDayData("Wed", 125),
    AqiDayData("Thu", 142),
    AqiDayData("Fri", 135),
    AqiDayData("Sat", 120),
    AqiDayData("Sun", 105),
)

@Composable
fun AirQualityDataScreen(
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
            // Top bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextDark)
                }
                Text(
                    text = "Air Quality Data",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextDark,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.width(48.dp))
            }

            Spacer(Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        text = "7-Day AQI Trend",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )

                    Spacer(Modifier.height(4.dp))

                    // AQI status badge
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(ChartOrange)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text("Moderate", fontSize = 13.sp, color = TextGray)
                    }

                    Spacer(Modifier.height(16.dp))

                    // ── CHART PLACEHOLDER ──
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp)
                            .background(Color(0xFFE8EEF8), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("📈  Chart goes here", fontSize = 14.sp,
                            color = ChartOrange, fontWeight = FontWeight.Medium)
                    }

                    Spacer(Modifier.height(16.dp))

                    // Daily AQI summary row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        aqiWeeklyData.forEach { entry ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(entry.day, fontSize = 10.sp, color = TextGray,
                                    textAlign = TextAlign.Center)
                                Text(
                                    text = "${entry.aqi}",
                                    fontSize = 11.sp,
                                    color = ChartOrange,
                                    fontWeight = FontWeight.SemiBold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(28.dp))

            Button(
                onClick = onSeeDetails,
                modifier = Modifier.fillMaxWidth(0.75f).height(52.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = CardWhite)
            ) {
                Text("See details", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextDark)
            }
        }
    }
}
