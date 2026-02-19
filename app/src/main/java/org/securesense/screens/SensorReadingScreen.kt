package org.securesense.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ─── Color Palette ───────────────────────────────────────────────────────────
private val BackgroundBlue  = Color(0xFF93C6E7)   // light sky blue background
private val CardWhite       = Color(0xFFF0F4FF)   // card surface
private val PrimaryBlue     = Color(0xFF3B6FD4)   // progress bar / link blue
private val TextGray        = Color(0xFF9A9EB5)   // label / subtitle text
private val TextDark        = Color(0xFF1A1D2E)   // large value text
private val AqiOrange       = Color(0xFFFF9800)   // AQI moderate dot

// ─── Data Models ─────────────────────────────────────────────────────────────
sealed class SensorCardData {
    data class TemperatureCard(
        val label: String = "Temperature",
        val value: String = "63/31",
        val subtitle: String = "Feels Like 40 F",
        val showLink: Boolean = true
    ) : SensorCardData()

    data class ProgressCard(
        val label: String,
        val value: String,
        val subtitle: String,
        val progress: Float,            // 0f..1f
        val showLink: Boolean = true
    ) : SensorCardData()

    data class AirQualityCard(
        val label: String = "Air Quality",
        val aqi: String = "AQI142",
        val status: String = "Moderate",
        val showLink: Boolean = true
    ) : SensorCardData()
}

// ─── Screen ──────────────────────────────────────────────────────────────────
@Composable
fun SensorReadingScreen(
    userName: String = "piyush",
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundBlue)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        // ── Header ──
        Text(
            text = "Good evening, $userName",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )
        Text(
            text = "Check out today's weather information",
            fontSize = 13.sp,
            color = TextGray,
            modifier = Modifier.padding(top = 2.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ── Title ──
        Text(
            text = "Sensor Reading",
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TextDark,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // ── Cards ──
        TemperatureSensorCard(
            value = "63/31",
            subtitle = "Feels Like 40 F"
        )
        Spacer(modifier = Modifier.height(16.dp))

        ProgressSensorCard(
            label = "Humidity",
            value = "82%",
            subtitle = "High moisture content",
            progress = 0.82f
        )
        Spacer(modifier = Modifier.height(16.dp))

        AirQualitySensorCard(
            aqi = "AQI142",
            status = "Moderate"
        )
        Spacer(modifier = Modifier.height(16.dp))

        ProgressSensorCard(
            label = "Light Intensity",
            value = "82%",
            subtitle = "High Moisture content",
            progress = 0.55f,
            showLink = false
        )
        Spacer(modifier = Modifier.height(16.dp))

        ProgressSensorCard(
            label = "Rainfall",
            value = "82%",
            subtitle = "High Moisture content",
            progress = 0.82f
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

// ─── Temperature Card ────────────────────────────────────────────────────────
@Composable
fun TemperatureSensorCard(
    value: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    SensorCard(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(text = "Temperature", fontSize = 13.sp, color = TextGray)
            Icon(
                painter = painterResource(id = android.R.drawable.ic_menu_compass),
                contentDescription = null,
                tint = PrimaryBlue,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )
        Text(text = subtitle, fontSize = 12.sp, color = TextGray)
        Spacer(modifier = Modifier.height(8.dp))
        TapForDetailsLink()
    }
}

// ─── Progress Card ───────────────────────────────────────────────────────────
@Composable
fun ProgressSensorCard(
    label: String,
    value: String,
    subtitle: String,
    progress: Float,
    showLink: Boolean = true,
    modifier: Modifier = Modifier
) {
    SensorCard(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(text = label, fontSize = 13.sp, color = TextGray)
            Icon(
                painter = painterResource(id = android.R.drawable.ic_menu_compass),
                contentDescription = null,
                tint = PrimaryBlue,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )
        Text(text = subtitle, fontSize = 12.sp, color = TextGray)
        Spacer(modifier = Modifier.height(10.dp))
        SensorProgressBar(progress = progress)
        if (showLink) {
            Spacer(modifier = Modifier.height(8.dp))
            TapForDetailsLink()
        }
    }
}

// ─── Air Quality Card ────────────────────────────────────────────────────────
@Composable
fun AirQualitySensorCard(
    aqi: String,
    status: String,
    modifier: Modifier = Modifier
) {
    SensorCard(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(text = "Air Quality", fontSize = 13.sp, color = TextGray)
            Icon(
                painter = painterResource(id = android.R.drawable.ic_menu_mapmode),
                contentDescription = null,
                tint = PrimaryBlue,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = aqi,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(AqiOrange)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = status, fontSize = 13.sp, color = TextGray)
        }
        Spacer(modifier = Modifier.height(8.dp))
        TapForDetailsLink()
    }
}

// ─── Reusable Card Container ─────────────────────────────────────────────────
@Composable
fun SensorCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp),
            content = content
        )
    }
}

// ─── Progress Bar ────────────────────────────────────────────────────────────
@Composable
fun SensorProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    height: Dp = 6.dp
) {
    LinearProgressIndicator(
        progress = { progress },
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(50)),
        color = PrimaryBlue,
        trackColor = Color(0xFFDDE6F5)
    )
}

// ─── "Tap for details →" Link ────────────────────────────────────────────────
@Composable
fun TapForDetailsLink(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Tap for details",
            fontSize = 13.sp,
            color = PrimaryBlue,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = null,
            tint = PrimaryBlue,
            modifier = Modifier.size(14.dp)
        )
    }
}

// ─── Preview ─────────────────────────────────────────────────────────────────
@Preview(showBackground = true, widthDp = 390, heightDp = 900)
@Composable
fun SensorReadingScreenPreview() {
    MaterialTheme {
        SensorReadingScreen()
    }
}