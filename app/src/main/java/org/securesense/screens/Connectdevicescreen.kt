package org.securesense.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import kotlinx.coroutines.delay
import org.securesense.R

private val BgBlue   = Color(0xFFCFE8F8)
private val TextDark = Color(0xFF1A2D5A)
private val GreenOk  = Color(0xFF2E7D32)

// ── State machine for the connection flow ────────────────────────────────────
private enum class ConnectionState {
    IDLE,           // "Connect Device" button, no second option
    CONNECTING,     // "Connecting..." spinner
    CONNECTED,      // brief "Connected ✓" flash
    READY,          // "Fetch Data" button + "Connect Another Device" appears
    FETCHING        // "Fetching..." spinner → then navigate
}

@Composable
fun ConnectDeviceScreen(
    paddingValues: PaddingValues,
    onConnectDevice: () -> Unit = {},        // navigates to SensorReadingScreen
    onConnectAnotherDevice: () -> Unit = {}  // navigates to SensorReadingScreen
) {
    var state by remember { mutableStateOf(ConnectionState.IDLE) }

    // Drive the automatic transitions after button taps
    LaunchedEffect(state) {
        when (state) {
            ConnectionState.CONNECTING -> {
                delay(2500)
                state = ConnectionState.CONNECTED
            }
            ConnectionState.CONNECTED -> {
                delay(1000)
                state = ConnectionState.READY
            }
            ConnectionState.FETCHING -> {
                delay(1500)
                onConnectDevice()           // navigate away + pop stack
            }
            else -> Unit
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgBlue)
            .padding(paddingValues)
            .padding(horizontal = 32.dp, vertical = 24.dp)
    ) {
        // ── Logo + Title ─────────────────────────────────────────────────────
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_securesense_logo),
                contentDescription = "SecureSense Logo",
                modifier = Modifier.size(180.dp)
            )
            Spacer(Modifier.height(20.dp))
            Text(
                text = "Smart Sensing. Secure Living.",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = TextDark.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                letterSpacing = 0.3.sp
            )
        }

        // ── Bottom actions ────────────────────────────────────────────────────
        Column(
            modifier = Modifier.align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ── Main button ──────────────────────────────────────────────────
            Button(
                onClick = {
                    when (state) {
                        ConnectionState.IDLE    -> state = ConnectionState.CONNECTING
                        ConnectionState.READY   -> state = ConnectionState.FETCHING
                        else                    -> Unit   // disable during transitions
                    }
                },
                enabled = state == ConnectionState.IDLE || state == ConnectionState.READY,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black,
                    disabledContainerColor = Color.White,
                    disabledContentColor = Color.Black
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                // Animate the label change
                AnimatedContent(
                    targetState = state,
                    transitionSpec = {
                        fadeIn(tween(300)) togetherWith fadeOut(tween(300))
                    },
                    label = "button_label"
                ) { currentState ->
                    when (currentState) {
                        ConnectionState.IDLE -> {
                            Text("Connect Device", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                        ConnectionState.CONNECTING -> {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp, color = TextDark)
                                Text("Connecting...", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        ConnectionState.CONNECTED -> {
                            Text("Connected ✓", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = GreenOk)
                        }
                        ConnectionState.READY -> {
                            Text("Fetch Data", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                        ConnectionState.FETCHING -> {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp, color = TextDark)
                                Text("Fetching...", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // ── "Connect Another Device" — only visible in READY state ──────
            AnimatedVisibility(
                visible = state == ConnectionState.READY || state == ConnectionState.FETCHING,
                enter = fadeIn(tween(400)) + slideInVertically(tween(400)) { it / 2 },
                exit  = fadeOut(tween(200))
            ) {
                TextButton(onClick = onConnectAnotherDevice) {
                    Text(
                        text = "Connect Another Device",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                }
            }


        }
    }
}