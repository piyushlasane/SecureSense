package org.securesense.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val BackgroundBlue = Color(0xFF93C6E7)
private val CardWhite      = Color(0xFFF0F4FF)
private val PrimaryBlue    = Color(0xFF3B6FD4)
private val TextGray       = Color(0xFF9A9EB5)
private val TextDark       = Color(0xFF1A1D2E)
private val AqiOrange      = Color(0xFFFF9800)

@Composable
fun SensorReadingScreen(
    paddingValues: PaddingValues,
    userName: String = "piyush",
    modifier: Modifier = Modifier,
    onNavigateToTemperature:    () -> Unit = {},
    onNavigateToHumidity:       () -> Unit = {},
    onNavigateToAirQuality:     () -> Unit = {},
    onNavigateToLightIntensity: () -> Unit = {},
    onNavigateToRainfall:       () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundBlue)
            .verticalScroll(rememberScrollState())
            .padding(paddingValues)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {

        Text(
            text = "Sensor Reading",
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TextDark,
            modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(20.dp))

        TemperatureSensorCard(value = "63/31", subtitle = "Feels Like 40 F", onClick = onNavigateToTemperature)
        Spacer(modifier = Modifier.height(16.dp))
        ProgressSensorCard(label = "Humidity", value = "82%", subtitle = "High moisture content", progress = 0.82f, onClick = onNavigateToHumidity)
        Spacer(modifier = Modifier.height(16.dp))
        AirQualitySensorCard(aqi = "AQI142", status = "Moderate", onClick = onNavigateToAirQuality)
        Spacer(modifier = Modifier.height(16.dp))
        ProgressSensorCard(label = "Light Intensity", value = "82%", subtitle = "High Moisture content", progress = 0.55f, onClick = onNavigateToLightIntensity)
        Spacer(modifier = Modifier.height(16.dp))
        ProgressSensorCard(label = "Rainfall", value = "82%", subtitle = "High Moisture content", progress = 0.82f, onClick = onNavigateToRainfall)
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun TemperatureSensorCard(value: String, subtitle: String, onClick: () -> Unit = {}, modifier: Modifier = Modifier) {
    SensorCard(modifier = modifier, onClick = onClick) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
            Text(text = "Temperature", fontSize = 13.sp, color = TextGray)
            Icon(painter = painterResource(id = android.R.drawable.ic_menu_compass), contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(22.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = value, fontSize = 36.sp, fontWeight = FontWeight.Bold, color = TextDark)
        Text(text = subtitle, fontSize = 12.sp, color = TextGray)
        Spacer(modifier = Modifier.height(8.dp))
        TapForDetailsLink()
    }
}

@Composable
fun ProgressSensorCard(label: String, value: String, subtitle: String, progress: Float, showLink: Boolean = true, onClick: () -> Unit = {}, modifier: Modifier = Modifier) {
    SensorCard(modifier = modifier, onClick = onClick) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
            Text(text = label, fontSize = 13.sp, color = TextGray)
            Icon(painter = painterResource(id = android.R.drawable.ic_menu_compass), contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(22.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = value, fontSize = 36.sp, fontWeight = FontWeight.Bold, color = TextDark)
        Text(text = subtitle, fontSize = 12.sp, color = TextGray)
        Spacer(modifier = Modifier.height(10.dp))
        SensorProgressBar(progress = progress)
        if (showLink) {
            Spacer(modifier = Modifier.height(8.dp))
            TapForDetailsLink()
        }
    }
}

@Composable
fun AirQualitySensorCard(aqi: String, status: String, onClick: () -> Unit = {}, modifier: Modifier = Modifier) {
    SensorCard(modifier = modifier, onClick = onClick) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
            Text(text = "Air Quality", fontSize = 13.sp, color = TextGray)
            Icon(painter = painterResource(id = android.R.drawable.ic_menu_mapmode), contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(22.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = aqi, fontSize = 36.sp, fontWeight = FontWeight.Bold, color = TextDark)
        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(AqiOrange))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = status, fontSize = 13.sp, color = TextGray)
        }
        Spacer(modifier = Modifier.height(8.dp))
        TapForDetailsLink()
    }
}

@Composable
fun SensorCard(modifier: Modifier = Modifier, onClick: () -> Unit = {}, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp), content = content)
    }
}

@Composable
fun SensorProgressBar(progress: Float, modifier: Modifier = Modifier, height: Dp = 6.dp) {
    LinearProgressIndicator(
        progress = { progress },
        modifier = modifier.fillMaxWidth().height(height).clip(RoundedCornerShape(50)),
        color = PrimaryBlue,
        trackColor = Color(0xFFDDE6F5)
    )
}

@Composable
fun TapForDetailsLink(modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Tap for details", fontSize = 13.sp, color = PrimaryBlue, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.width(4.dp))
        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(14.dp))
    }
}