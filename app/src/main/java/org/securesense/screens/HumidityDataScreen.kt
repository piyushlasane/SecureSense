package org.securesense.screenpackage

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

private val BgBlue     = Color(0xFF93C6E7)
private val CardWhite  = Color(0xFFF0F4FF)
private val ChartBlue  = Color(0xFF3B6FD4)
private val ChartFill  = Color(0xFF3B6FD4).copy(alpha = 0.15f)
private val GridColor  = Color(0xFFDDE6F5)
private val TextDark   = Color(0xFF1A1D2E)
private val TextGray   = Color(0xFF9A9EB5)
private val AccentBlue = Color(0xFF3B6FD4)

data class HumidityEntry(val hour: String, val weatherIcon: WeatherIcon, val humidity: Int)

enum class WeatherIcon { SUNNY, CLOUDY, RAIN }

private val defaultEntries = listOf(
    HumidityEntry("9AM",  WeatherIcon.SUNNY,  10),
    HumidityEntry("10AM", WeatherIcon.SUNNY,  15),
    HumidityEntry("11AM", WeatherIcon.SUNNY,  20),
    HumidityEntry("12PM", WeatherIcon.CLOUDY, 35),
    HumidityEntry("1PM",  WeatherIcon.CLOUDY, 45),
    HumidityEntry("2PM",  WeatherIcon.RAIN,   60),
    HumidityEntry("3PM",  WeatherIcon.RAIN,   70),
    HumidityEntry("4PM",  WeatherIcon.CLOUDY, 50),
    HumidityEntry("5PM",  WeatherIcon.SUNNY,  30),
)

@Composable
fun HumidityDataScreen(
    paddingValues: PaddingValues,
    initialEntries: List<HumidityEntry> = defaultEntries,
    onBack: () -> Unit = {},
    onSeeDetails: () -> Unit = {}
) {
    var entries by remember { mutableStateOf(initialEntries) }
    var editIndex by remember { mutableStateOf(-1) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgBlue)
            .padding(paddingValues)                          // ← Scaffold insets
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextDark)
                }
                Text(text = "Humidity Data", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = TextDark, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                Spacer(Modifier.width(48.dp))
            }

            Spacer(Modifier.height(20.dp))

            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = CardWhite), elevation = CardDefaults.cardElevation(0.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Upcoming Hours", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextDark)
                    Spacer(Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.SpaceBetween) {
                        entries.forEach { entry ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(52.dp)) {
                                WeatherIconComposable(entry.weatherIcon)
                                Text(text = entry.hour, fontSize = 11.sp, color = TextGray, textAlign = TextAlign.Center)
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    HumidityLineChart(
                        entries = entries,
                        modifier = Modifier.fillMaxWidth().height(200.dp),
                        onBarTapped = { index -> editIndex = index }
                    )

                    Spacer(Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.SpaceBetween) {
                        entries.forEach { entry ->
                            Text(text = "${entry.humidity}%", fontSize = 12.sp, color = AccentBlue, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center, modifier = Modifier.width(52.dp))
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Button(onClick = onSeeDetails, modifier = Modifier.fillMaxWidth(0.75f).height(52.dp), shape = RoundedCornerShape(50), colors = ButtonDefaults.buttonColors(containerColor = CardWhite)) {
                Text("See details", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextDark)
            }
        }

        if (editIndex >= 0) {
            SingleEntryEditDialog(
                entry = entries[editIndex],
                onDismiss = { editIndex = -1 },
                onConfirm = { updated ->
                    entries = entries.toMutableList().also { it[editIndex] = updated }
                    editIndex = -1
                }
            )
        }
    }
}

@Composable
fun HumidityLineChart(entries: List<HumidityEntry>, modifier: Modifier = Modifier, onBarTapped: (Int) -> Unit = {}) {
    val maxValue = 80f
    val yLabels = listOf(80, 60, 40, 20, 0)
    val animatedValues = entries.map { entry ->
        animateFloatAsState(targetValue = entry.humidity.toFloat(), animationSpec = tween(600), label = "h_${entry.hour}").value
    }

    Row(modifier = modifier) {
        Column(modifier = Modifier.width(32.dp).fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween) {
            yLabels.forEach { label -> Text(text = label.toString(), fontSize = 10.sp, color = TextGray, textAlign = TextAlign.End, modifier = Modifier.fillMaxWidth()) }
        }
        Spacer(Modifier.width(4.dp))
        Column(modifier = Modifier.weight(1f)) {
            Canvas(
                modifier = Modifier.fillMaxWidth().weight(1f).pointerInput(entries) {
                    detectTapGestures { offset ->
                        val segmentWidth = size.width.toFloat() / (entries.size - 1)
                        onBarTapped((offset.x / segmentWidth).toInt().coerceIn(0, entries.size - 1))
                    }
                }
            ) {
                drawHumidityChart(animatedValues, maxValue, ChartBlue, ChartFill, GridColor)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                entries.forEach { entry -> Text(text = entry.hour, fontSize = 9.sp, color = TextGray, textAlign = TextAlign.Center, modifier = Modifier.weight(1f)) }
            }
        }
    }
}

private fun DrawScope.drawHumidityChart(values: List<Float>, maxValue: Float, chartBlue: Color, chartFill: Color, gridColor: Color) {
    val w = size.width; val h = size.height; val n = values.size
    if (n < 2) return
    for (i in 0..4) { val y = h * i / 4; drawLine(gridColor, Offset(0f, y), Offset(w, y), 1.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f))) }
    fun xAt(i: Int) = w * i / (n - 1)
    fun yAt(v: Float) = h - (v / maxValue * h).coerceIn(0f, h)
    val points = values.mapIndexed { i, v -> Offset(xAt(i), yAt(v)) }
    val linePath = Path().apply {
        moveTo(points[0].x, points[0].y)
        for (i in 1 until points.size) { val p = points[i-1]; val c = points[i]; val cx = (p.x+c.x)/2f; cubicTo(cx, p.y, cx, c.y, c.x, c.y) }
    }
    drawPath(Path().apply { addPath(linePath); lineTo(points.last().x, h); lineTo(points.first().x, h); close() }, Brush.verticalGradient(listOf(chartFill, Color.Transparent), 0f, h))
    drawPath(linePath, chartBlue, style = Stroke(2.5.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))
    points.forEach { drawCircle(chartBlue, 4.dp.toPx(), it); drawCircle(Color.White, 2.dp.toPx(), it) }
}

@Composable
fun WeatherIconComposable(icon: WeatherIcon) {
    Text(text = when (icon) { WeatherIcon.SUNNY -> "☀️"; WeatherIcon.CLOUDY -> "☁️"; WeatherIcon.RAIN -> "🌧️" }, fontSize = 20.sp, textAlign = TextAlign.Center)
}

@Composable
fun SingleEntryEditDialog(entry: HumidityEntry, onDismiss: () -> Unit, onConfirm: (HumidityEntry) -> Unit) {
    var humidityText by remember { mutableStateOf(entry.humidity.toString()) }
    var selectedIcon by remember { mutableStateOf(entry.weatherIcon) }
    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = CardWhite)) {
            Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Edit ${entry.hour}", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextDark)
                OutlinedTextField(value = humidityText, onValueChange = { if (it.length <= 3) humidityText = it }, label = { Text("Humidity %") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true, modifier = Modifier.fillMaxWidth())
                Text("Weather icon:", fontSize = 13.sp, color = TextGray)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    WeatherIcon.values().forEach { icon ->
                        FilterChip(selected = selectedIcon == icon, onClick = { selectedIcon = icon }, label = { Text(when (icon) { WeatherIcon.SUNNY -> "☀️"; WeatherIcon.CLOUDY -> "☁️"; WeatherIcon.RAIN -> "🌧️" }, fontSize = 18.sp) })
                    }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = { onConfirm(entry.copy(humidity = humidityText.toIntOrNull()?.coerceIn(0, 100) ?: entry.humidity, weatherIcon = selectedIcon)) }) { Text("Save") }
                }
            }
        }
    }
}