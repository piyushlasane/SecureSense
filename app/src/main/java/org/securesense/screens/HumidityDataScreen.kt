package org.securesense.screenpackage

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.horizontalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

// ─── Color Palette ───────────────────────────────────────────────────────────
private val BgBlue       = Color(0xFF93C6E7)
private val CardWhite    = Color(0xFFF0F4FF)
private val ChartBlue    = Color(0xFF3B6FD4)
private val ChartFill    = Color(0xFF3B6FD4).copy(alpha = 0.15f)
private val GridColor    = Color(0xFFDDE6F5)
private val TextDark     = Color(0xFF1A1D2E)
private val TextGray     = Color(0xFF9A9EB5)
private val AccentBlue   = Color(0xFF3B6FD4)

// ─── Data Model ──────────────────────────────────────────────────────────────
data class HumidityEntry(
    val hour: String,
    val weatherIcon: WeatherIcon,
    val humidity: Int          // 0–100
)

enum class WeatherIcon { SUNNY, CLOUDY, RAIN }

// ─── Default Data ────────────────────────────────────────────────────────────
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

// ─── Screen ──────────────────────────────────────────────────────────────────
@Composable
fun HumidityDataScreen(
    initialEntries: List<HumidityEntry> = defaultEntries,
    onSeeDetails: () -> Unit = {}
) {
    var entries by remember { mutableStateOf(initialEntries) }
    var showEditDialog by remember { mutableStateOf(false) }
    var editIndex by remember { mutableStateOf(-1) }

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
            // ── Title ──
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Humidity Data",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextDark
                )
            }

            Spacer(Modifier.height(20.dp))

            // ── Main Card ──
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
                    Spacer(Modifier.height(12.dp))

                    // ── Hour + Icon Row ──
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        entries.forEach { entry ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.width(52.dp)
                            ) {
                                WeatherIconComposable(entry.weatherIcon)
                                Text(
                                    text = entry.hour,
                                    fontSize = 11.sp,
                                    color = TextGray,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    // ── Chart ──
                    HumidityLineChart(
                        entries = entries,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        onBarTapped = { index ->
                            editIndex = index
                        }
                    )

                    Spacer(Modifier.height(12.dp))

                    // ── Humidity % Row ──
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        entries.forEach { entry ->
                            Text(
                                text = "${entry.humidity}%",
                                fontSize = 12.sp,
                                color = AccentBlue,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width(52.dp)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

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

        // ── Tap on chart point to edit single entry ──
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

        // ── Full data edit dialog ──
        if (showEditDialog) {
            FullDataEditDialog(
                entries = entries,
                onDismiss = { showEditDialog = false },
                onConfirm = { updated ->
                    entries = updated
                    showEditDialog = false
                }
            )
        }
    }
}

// ─── Line Chart ──────────────────────────────────────────────────────────────
@Composable
fun HumidityLineChart(
    entries: List<HumidityEntry>,
    modifier: Modifier = Modifier,
    onBarTapped: (Int) -> Unit = {}
) {
    val maxValue = 80f
    val yLabels = listOf(80, 60, 40, 20, 0)

    // Animate each point
    val animatedValues = entries.map { entry ->
        animateFloatAsState(
            targetValue = entry.humidity.toFloat(),
            animationSpec = tween(durationMillis = 600),
            label = "humidity_${entry.hour}"
        ).value
    }

    Row(modifier = modifier) {
        // Y-axis labels
        Column(
            modifier = Modifier
                .width(32.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            yLabels.forEach { label ->
                Text(
                    text = label.toString(),
                    fontSize = 10.sp,
                    color = TextGray,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(Modifier.width(4.dp))

        Column(modifier = Modifier.weight(1f)) {
            // Chart canvas
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .pointerInput(entries) {
                        detectTapGestures { offset ->
                            val segmentWidth = size.width.toFloat() / (entries.size - 1)
                            val index = (offset.x / segmentWidth).toInt().coerceIn(0, entries.size - 1)
                            onBarTapped(index)
                        }
                    }
            ) {
                drawHumidityChart(
                    values = animatedValues,
                    maxValue = maxValue,
                    chartBlue = ChartBlue,
                    chartFill = ChartFill,
                    gridColor = GridColor
                )
            }

            // X-axis labels
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                entries.forEach { entry ->
                    Text(
                        text = entry.hour,
                        fontSize = 9.sp,
                        color = TextGray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

private fun DrawScope.drawHumidityChart(
    values: List<Float>,
    maxValue: Float,
    chartBlue: Color,
    chartFill: Color,
    gridColor: Color
) {
    val w = size.width
    val h = size.height
    val n = values.size
    if (n < 2) return

    // Grid lines (horizontal)
    val gridCount = 4
    for (i in 0..gridCount) {
        val y = h * i / gridCount
        drawLine(
            color = gridColor,
            start = Offset(0f, y),
            end = Offset(w, y),
            strokeWidth = 1.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f))
        )
    }

    // Build path points
    fun xAt(i: Int) = w * i / (n - 1)
    fun yAt(v: Float) = h - (v / maxValue * h).coerceIn(0f, h)

    val points = values.mapIndexed { i, v -> Offset(xAt(i), yAt(v)) }

    // Smooth curve using cubic bezier
    val linePath = Path().apply {
        moveTo(points[0].x, points[0].y)
        for (i in 1 until points.size) {
            val prev = points[i - 1]
            val curr = points[i]
            val cpX = (prev.x + curr.x) / 2f
            cubicTo(cpX, prev.y, cpX, curr.y, curr.x, curr.y)
        }
    }

    // Fill path
    val fillPath = Path().apply {
        addPath(linePath)
        lineTo(points.last().x, h)
        lineTo(points.first().x, h)
        close()
    }
    drawPath(
        path = fillPath,
        brush = Brush.verticalGradient(
            colors = listOf(chartFill, Color.Transparent),
            startY = 0f,
            endY = h
        )
    )

    // Draw line
    drawPath(
        path = linePath,
        color = chartBlue,
        style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
    )

    // Draw dots on each point
    points.forEach { pt ->
        drawCircle(color = chartBlue, radius = 4.dp.toPx(), center = pt)
        drawCircle(color = Color.White, radius = 2.dp.toPx(), center = pt)
    }
}

// ─── Weather Icon ─────────────────────────────────────────────────────────────
@Composable
fun WeatherIconComposable(icon: WeatherIcon) {
    val emoji = when (icon) {
        WeatherIcon.SUNNY  -> "☀️"
        WeatherIcon.CLOUDY -> "☁️"
        WeatherIcon.RAIN   -> "🌧️"
    }
    Text(text = emoji, fontSize = 20.sp, textAlign = TextAlign.Center)
}

// ─── Single Entry Edit Dialog ────────────────────────────────────────────────
@Composable
fun SingleEntryEditDialog(
    entry: HumidityEntry,
    onDismiss: () -> Unit,
    onConfirm: (HumidityEntry) -> Unit
) {
    var humidityText by remember { mutableStateOf(entry.humidity.toString()) }
    var selectedIcon by remember { mutableStateOf(entry.weatherIcon) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = CardWhite)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Edit ${entry.hour}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TextDark
                )

                OutlinedTextField(
                    value = humidityText,
                    onValueChange = { if (it.length <= 3) humidityText = it },
                    label = { Text("Humidity %") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Weather icon:", fontSize = 13.sp, color = TextGray)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    WeatherIcon.values().forEach { icon ->
                        val emoji = when (icon) {
                            WeatherIcon.SUNNY  -> "☀️"
                            WeatherIcon.CLOUDY -> "☁️"
                            WeatherIcon.RAIN   -> "🌧️"
                        }
                        FilterChip(
                            selected = selectedIcon == icon,
                            onClick = { selectedIcon = icon },
                            label = { Text(emoji, fontSize = 18.sp) }
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = {
                        val h = humidityText.toIntOrNull()?.coerceIn(0, 100) ?: entry.humidity
                        onConfirm(entry.copy(humidity = h, weatherIcon = selectedIcon))
                    }) { Text("Save") }
                }
            }
        }
    }
}

// ─── Full Data Edit Dialog ────────────────────────────────────────────────────
@Composable
fun FullDataEditDialog(
    entries: List<HumidityEntry>,
    onDismiss: () -> Unit,
    onConfirm: (List<HumidityEntry>) -> Unit
) {
    var editableEntries by remember { mutableStateOf(entries.map { it.humidity.toString() }) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = CardWhite),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    "Edit All Humidity Values",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TextDark
                )
                Spacer(Modifier.height(12.dp))

                entries.forEachIndexed { index, entry ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = entry.hour,
                            modifier = Modifier.width(52.dp),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextDark
                        )
                        Spacer(Modifier.width(8.dp))
                        OutlinedTextField(
                            value = editableEntries[index],
                            onValueChange = { newVal ->
                                if (newVal.length <= 3) {
                                    editableEntries = editableEntries.toMutableList()
                                        .also { it[index] = newVal }
                                }
                            },
                            label = { Text("Humidity %") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = {
                        val updated = entries.mapIndexed { i, entry ->
                            entry.copy(
                                humidity = editableEntries[i].toIntOrNull()
                                    ?.coerceIn(0, 100) ?: entry.humidity
                            )
                        }
                        onConfirm(updated)
                    }) { Text("Save All") }
                }
            }
        }
    }
}
